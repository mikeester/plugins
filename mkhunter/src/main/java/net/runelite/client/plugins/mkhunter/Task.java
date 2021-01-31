package net.runelite.client.plugins.mkhunter;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkutils.CalculationUtils;
import net.runelite.client.plugins.mkutils.DialogUtils;
import net.runelite.client.plugins.mkutils.EquipmentUtils;
import net.runelite.client.plugins.mkutils.InventoryUtils;
import net.runelite.client.plugins.mkutils.KeyboardUtils;
import net.runelite.client.plugins.mkutils.MKUtils;
import net.runelite.client.plugins.mkutils.MenuUtils;
import net.runelite.client.plugins.mkutils.MouseUtils;
import net.runelite.client.plugins.mkutils.NPCUtils;
import net.runelite.client.plugins.mkutils.ObjectUtils;
import net.runelite.client.plugins.mkutils.PlayerUtils;
import net.runelite.client.plugins.mkutils.WalkUtils;

import javax.inject.Inject;

@Slf4j
public abstract class Task
{
	public Task()
	{
	}

	@Inject
	public Client client;

	@Inject
	public Injector injector;

	@Inject
	public MKHunterConfig config;

	@Inject
	public MKUtils utils;

	@Inject
	public KeyboardUtils keyboard;

	@Inject
	public MenuUtils menu;

	@Inject
	public MouseUtils mouse;

	@Inject
	public InventoryUtils inventory;

	@Inject
	public CalculationUtils calc;

	@Inject
	public PlayerUtils playerUtils;

	@Inject
	public NPCUtils npcUtils;

	@Inject
	public ObjectUtils object;

	@Inject
	public WalkUtils walking;

	@Inject
	public MKHunterPlugin plugin;

	@Inject
	public EquipmentUtils equipmentUtils;

	@Inject
	public DialogUtils dialogUtils;

	public static MenuEntry entry;
	public static String status = "";

	public abstract boolean validate();

	public long sleepDelay()
	{
		MKHunterPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
		return MKHunterPlugin.sleepLength;
	}

	public int tickDelay()
	{
		MKHunterPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
		return MKHunterPlugin.tickLength;
	}

	public String getTaskDescription()
	{
		return this.getClass().getSimpleName();
	}

	public void onGameTick(GameTick event)
	{
		return;
	}



}
