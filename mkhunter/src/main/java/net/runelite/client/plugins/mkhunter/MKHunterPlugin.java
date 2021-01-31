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
package net.runelite.client.plugins.mkhunter;

import com.google.inject.Injector;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.XpDropEvent;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.mkhunter.tasks.CloseLevelUpDialogTask;
import net.runelite.client.plugins.mkhunter.tasks.MovingTask;
import net.runelite.client.plugins.mkhunter.tasks.StopTask;
import net.runelite.client.plugins.mkhunter.tasks.TimeoutTask;
import net.runelite.client.plugins.mkhunter.tasks.falconry.CatchKebbitTask;
import net.runelite.client.plugins.mkhunter.tasks.falconry.DropLootTask;
import net.runelite.client.plugins.mkhunter.tasks.falconry.GetFalconTask;
import net.runelite.client.plugins.mkhunter.tasks.falconry.RetrieveFalconTask;
import net.runelite.client.plugins.mkhunter.tasks.monkeys.CastBonesToBananasTask;
import net.runelite.client.plugins.mkhunter.tasks.monkeys.CheckTrapTask;
import net.runelite.client.plugins.mkhunter.tasks.monkeys.ClimbMonkeyTask;
import net.runelite.client.plugins.mkhunter.tasks.monkeys.DropItemTask;
import net.runelite.client.plugins.mkhunter.tasks.monkeys.PickUpBoneTask;
import net.runelite.client.plugins.mkhunter.tasks.monkeys.SetTrapTask;
import net.runelite.client.plugins.mkhunter.tasks.monkeys.WalkToBoulderTask;
import net.runelite.client.plugins.mkutils.MKUtils;
import net.runelite.client.plugins.mkutils.MenuUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;


@Extension
@PluginDependency(MKUtils.class)
@PluginDescriptor(
	name = "MK Hunter",
	enabledByDefault = false,
	description = "A plugin to help you train hunter",
	tags = {"mikester", "mk", "hunter"},
	type = PluginType.SKILLING
)
@Slf4j
public class MKHunterPlugin extends Plugin
{
	@Inject
	private Injector injector;

	@Inject
	private Client client;

	@Inject
	private MKHunterConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MKHunterOverlay overlay;

	@Inject
	private MKUtils utils;

	@Inject
	private ConfigManager configManager;

	@Inject
	private MenuUtils menu;

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

	public boolean lostFalcon;
	public int caught;
	public long caughtPerHour;


	@Provides
	MKHunterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MKHunterConfig.class);
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
		if (config.methodChoice() == MKHunterConfig.MethodChoice.FALCONRY)
		{
			tasks.addAll(
					injector.getInstance(StopTask.class),
					injector.getInstance(TimeoutTask.class),
					injector.getInstance(CloseLevelUpDialogTask.class),
					injector.getInstance(DropLootTask.class),
					injector.getInstance(RetrieveFalconTask.class),
					injector.getInstance(CatchKebbitTask.class),
					injector.getInstance(GetFalconTask.class)
			);
		}
		else if (config.methodChoice() == MKHunterConfig.MethodChoice.MONKEYS)
		{
			tasks.addAll(
					injector.getInstance(StopTask.class),
					injector.getInstance(TimeoutTask.class),
					injector.getInstance(MovingTask.class),
					injector.getInstance(CloseLevelUpDialogTask.class),
					injector.getInstance(WalkToBoulderTask.class),
					injector.getInstance(ClimbMonkeyTask.class),
					injector.getInstance(CheckTrapTask.class),
					injector.getInstance(SetTrapTask.class),
					injector.getInstance(DropItemTask.class),
					injector.getInstance(CastBonesToBananasTask.class),
					injector.getInstance(PickUpBoneTask.class)
			);
		}
	}

	public void resetVals()
	{
		log.debug("stopping MK Hunter plugin");
		overlayManager.remove(overlay);

		startBot = false;
		botTimer = null;
		tasks.clear();
	}

	@Subscribe
	private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked)
	{
		if (!configButtonClicked.getGroup().equalsIgnoreCase("mkhunter"))
		{
			return;
		}
		log.debug("button {} pressed!", configButtonClicked.getKey());
		if (configButtonClicked.getKey().equals("startButton"))
		{
			if (!startBot)
			{
				Player player = client.getLocalPlayer();
				if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN)
				{
					log.info("starting MK Hunter plugin");
					loadTasks();
					startBot = true;
					caught = 0;
					timeout = 0;
					lostFalcon = false;
					targetMenu = null;
					botTimer = Instant.now();
					overlayManager.add(overlay);
					beforeLoc = client.getLocalPlayer().getLocalLocation();
				}
				else
				{
					log.info("[MK] Start logged in");
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

	public boolean hasTrapSetup()
	{
		GameObject setupTrap = new GameObjectQuery()
				.idEquals(MKHunterData.SETUP_BOULDER, MKHunterData.SETUP_ANIM_BOULDER)
				.atWorldLocation(MKHunterData.BOULDER_TILE)
				.result(client)
				.first();

		return setupTrap != null;
	}

	public boolean hasTrapCaught()
	{
		GameObject caughtTrap = new GameObjectQuery()
				.idEquals(MKHunterData.CAUGHT_EAST, MKHunterData.CAUGHT_SOUTH)
				.atWorldLocation(MKHunterData.BOULDER_TILE)
				.result(client)
				.first();

		return caughtTrap != null;
	}

	public boolean canSetupTrap()
	{
		GameObject idleBoulder = new GameObjectQuery()
				.idEquals(MKHunterData.EMPTY_BOULDER)
				.atWorldLocation(MKHunterData.BOULDER_TILE)
				.result(client)
				.first();

		return idleBoulder != null;
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		if (!startBot)
		{
			return;
		}

		if (event.getMessage().contains("heading back toward the falconer"))
		{

		}
	}

	public boolean falconIsFlyingToTarget()
	{
		if (client.getProjectiles() == null || client.getProjectiles().isEmpty())
		{
			return false;
		}

		Player local = client.getLocalPlayer();
		if (local == null)
		{
			return false;
		}

		for (Projectile p : client.getProjectiles())
		{
			if (p.getId() == 922)
			{
				WorldPoint origin = new WorldPoint(p.getX1(), p.getY1(), p.getFloor());
				// Not 100% accurate as another nearby player could have sent this, however we want some tolerance
				// in case we were moving. There might be a way to find the origin Actor...
				return origin.distanceTo(local.getWorldLocation()) < 2;
			}
		}

		return false;
	}

	@Subscribe
	private void onXpDropEvent(XpDropEvent event)
	{
		if (!startBot)
		{
			return;
		}

		if (event.getSkill() == Skill.HUNTER)
		{
			caught++;
			caughtPerHour = getPerHour(caught);
		}
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
						timeoutFinished = false;
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
			}

			beforeLoc = player.getLocalLocation();
		}
	}

}