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
package net.runelite.client.plugins.hidetanner;

import com.google.inject.Injector;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.hidetanner.tasks.ShouldStop;
import net.runelite.client.plugins.mkutils.MKUtils;
import net.runelite.client.plugins.mkutils.MenuUtils;
import net.runelite.client.plugins.mkutils.ObjectUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;


@Extension
@PluginDependency(MKUtils.class)
@PluginDescriptor(
	name = "MK Hide Tanner",
	enabledByDefault = false,
	description = "Tan hides in Al Kharid",
	tags = {"mikester", "mk", "moneymaking"},
	type = PluginType.UNCATEGORIZED
)
@Slf4j
public class MKHideTannerPlugin extends Plugin
{
	@Inject
	private Injector injector;

	@Inject
	private Client client;

	@Inject
	private MKHideTannerConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MKHideTannerOverlay overlay;

	@Inject
	private ObjectUtils objectUtils;

	@Inject
	private MKUtils utils;

	@Inject
	private ConfigManager configManager;

	@Inject
	private MenuUtils menu;

	private TreeSet treeTasks = new TreeSet();
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
	public static boolean isBanking;

	public static int hideID = ItemID.GREEN_DRAGONHIDE;
	public static int hideInterfaceChildID = 128;



	@Provides
	MKHideTannerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MKHideTannerConfig.class);
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
		treeTasks.setRoot(injector.getInstance(ShouldStop.class));
	}

	public void resetVals()
	{
		log.debug("stopping MK Hide Tanner plugin");
		overlayManager.remove(overlay);

		startBot = false;
		shouldStop = false;
		botTimer = null;
		treeTasks.setRoot(null);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equalsIgnoreCase("MK Hide Tanner"))
		{
			return;
		}

		switch (event.getKey())
		{
			case "leatherChoice":
				log.info("New: " + event.getNewValue());
				for (MKHideTannerConfig.LeatherChoice lt : MKHideTannerConfig.LeatherChoice.values())
				{

					if (lt.name().equalsIgnoreCase(event.getNewValue()))
					{
						hideID = lt.hideId;
						hideInterfaceChildID = lt.widgetChildId;
						log.info("[MK] Set leather to " + event.getNewValue() + ", (" + hideID + ", " + hideInterfaceChildID + ")");
						return;
					}
				}
		}

	}

	@Subscribe
	private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked)
	{
		if (!configButtonClicked.getGroup().equalsIgnoreCase("MK Hide Tanner"))
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
					log.info("Starting hide tanner plugin");
					startBot = true;
					loadTasks();

					timeout = 0;
					targetMenu = null;
					botTimer = Instant.now();
					overlayManager.add(overlay);
					beforeLoc = client.getLocalPlayer().getLocalLocation();
				}
				else
				{
					log.info("Start logged in");
				}
			}
			else
			{
				resetVals();
			}
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
			LeafTask nextLeaf = treeTasks.getLeafTask();

			if (nextLeaf != null)
			{
				log.info("Executing task: " + nextLeaf.getClass().getSimpleName());
				nextLeaf.onGameTick(event);
			}

			beforeLoc = player.getLocalLocation();
		}
	}

}