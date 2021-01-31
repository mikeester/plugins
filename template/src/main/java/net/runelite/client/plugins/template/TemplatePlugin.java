/*
 * Copyright (c) 2018, mikeester <https://github.com/mikeester>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.template;

import com.google.inject.Injector;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.mkutils.MKUtils;
import net.runelite.client.plugins.mkutils.MenuUtils;
import net.runelite.client.plugins.template.tasks.MovingTask;
import net.runelite.client.plugins.template.tasks.StopTask;
import net.runelite.client.plugins.template.tasks.TimeoutTask;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;


@Extension
@PluginDependency(MKUtils.class)
@PluginDescriptor(
	name = "MK Template",
	enabledByDefault = false,
	description = "Use this as a base for plugins",
	tags = {"mikester", "mk"},
	type = PluginType.MISCELLANEOUS
)
@Slf4j
public class TemplatePlugin extends Plugin
{
	@Inject
	private Injector injector;

	@Inject
	private Client client;

	@Inject
	private TemplateConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TemplateOverlay overlay;

	@Inject
	private MKUtils utils;

	@Inject
	private ConfigManager configManager;

	@Inject
	private MenuUtils menu;

	@Inject
	private ActionQueue actionQueue;

	private TaskSet tasks = new TaskSet();
	public static LocalPoint beforeLoc = new LocalPoint(0, 0);

	MenuEntry targetMenu;
	Instant botTimer;
	Player player;

	public static boolean startBot;
	public static boolean shouldStop;
	public static long sleepLength;
	public static int tickLength;
	public static int timeout;
	public static String status = "Starting...";
	public static ConditionTimeout conditionTimeout;
	public static boolean timeoutFinished;


	@Provides
	TemplateConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TemplateConfig.class);
	}

	@Override
	protected void startUp()
	{

	}

	@Override
	protected void shutDown()
	{
		resetVals();

	}


	private void loadTasks()
	{
		tasks.clear();
		tasks.addAll(
			injector.getInstance(StopTask.class),
			injector.getInstance(TimeoutTask.class),
			injector.getInstance(MovingTask.class)
		);
	}

	public void resetVals()
	{
		log.debug("stopping Task Template plugin");
		overlayManager.remove(overlay);

		startBot = false;
		shouldStop = false;
		botTimer = null;
		tasks.clear();
	}

	@Subscribe
	private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked)
	{
		if (!configButtonClicked.getGroup().equalsIgnoreCase("Template"))
		{
			return;
		}
		if (configButtonClicked.getKey().equals("startButton"))
		{
			if (!startBot)
			{
				Player player = client.getLocalPlayer();
				if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN)
				{
					log.info("[MK] Starting template plugin");
					loadTasks();
					timeoutFinished = false;
					startBot = true;
					// Clear action queue in case some were left over
					if (actionQueue.delayedActions.size() > 0)
					{
						log.info("[MK] Clearing " +  actionQueue.delayedActions.size() + " actions.");
						actionQueue.delayedActions.clear();
					}
					timeout = 0;
					targetMenu = null;
					botTimer = Instant.now();
					overlayManager.add(overlay);
					beforeLoc = client.getLocalPlayer().getLocalLocation();
				}
				else
				{
					log.info("[MK] Start the plugin logged in.");
				}
			}
			else
			{
				resetVals();
			}
		}
	}

	public void updateStats()
	{
		//templatePH = (int) getPerHour(totalBraceletCount);
		//coinsPH = (int) getPerHour(totalCoins - ((totalCoins / BRACELET_HA_VAL) * (unchargedBraceletCost + revEtherCost + natureRuneCost)));
	}

	public long getPerHour(int quantity)
	{
		Duration timeSinceStart = Duration.between(botTimer, Instant.now());
		if (!timeSinceStart.isZero())
		{
			return (int) ((double) quantity * (double) Duration.ofHours(1).toMillis() / (double) timeSinceStart.toMillis());
		}
		return 0;
	}

	@Subscribe
	private void onGameTick(GameTick event)
	{
		if (!startBot)
		{
			return;
		}
		player = client.getLocalPlayer();
		if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN)
		{


			Task task = tasks.getValidTask();

			if (task != null)
			{
				task.onGameTick(event);
				status = task.getTaskDescription();

				// If this was a timeout task, and the timeout ended on this tick, we can do an action for this tick
				if (timeoutFinished)
				{

					// Don't do an action if a tick timeout was also set
					if (timeout > 0)
					{
						return;
					}

					Task newTask = tasks.getValidTask();
					if (newTask != null)
					{
						newTask.onGameTick(event);
						status = task.getTaskDescription();
					}
					else
					{
						status = "Idle";
					}

					timeoutFinished = false;
				}
			}
			else
			{
				status = "Idle";
				log.debug(status);
			}

			beforeLoc = player.getLocalLocation();
		}
	}

}