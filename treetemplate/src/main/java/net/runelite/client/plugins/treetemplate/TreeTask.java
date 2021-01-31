package net.runelite.client.plugins.treetemplate;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkutils.BankUtils;
import net.runelite.client.plugins.mkutils.CalculationUtils;
import net.runelite.client.plugins.mkutils.InterfaceUtils;
import net.runelite.client.plugins.mkutils.InventoryUtils;
import net.runelite.client.plugins.mkutils.KeyboardUtils;
import net.runelite.client.plugins.mkutils.MenuUtils;
import net.runelite.client.plugins.mkutils.MouseUtils;
import net.runelite.client.plugins.mkutils.NPCUtils;
import net.runelite.client.plugins.mkutils.ObjectUtils;
import net.runelite.client.plugins.mkutils.PlayerUtils;
import net.runelite.client.plugins.mkutils.WalkUtils;
import net.runelite.client.plugins.mkutils.MKUtils;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

@Slf4j
public abstract class TreeTask {

    public TreeTask() {}

    @Inject
    public Injector injector;

    @Inject
    public Client client;

    @Inject
    public TreeTemplateConfig config;

    @Inject
    public TreeTemplatePlugin plugin;

    @Inject
    public MKUtils utils;

    @Inject
    public BankUtils bank;

    @Inject
    public KeyboardUtils keyboard;

    @Inject
    public MenuUtils menu;

    @Inject
    public WalkUtils walk;

    @Inject
    public MouseUtils mouse;

    @Inject
    public InventoryUtils inventory;

    @Inject
    public InterfaceUtils interfaceUtils;

    @Inject
    public CalculationUtils calc;

    @Inject
    public PlayerUtils playerUtils;

    @Inject
    public NPCUtils npcUtils;

    @Inject
    public ObjectUtils object;

    @Inject
    public ExecutorService executorService;

    public static String status;

    public long sleepDelay()
    {
        TreeTemplatePlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
        return TreeTemplatePlugin.sleepLength;
    }

    public int tickDelay()
    {
        TreeTemplatePlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
        return TreeTemplatePlugin.tickLength;
    }

    public String getTaskDescription()
    {
        return this.getClass().getSimpleName();
    }

    public void onGameTick(GameTick event) { return; }

    public abstract boolean validate();

    public abstract TreeTask successTask();

    public abstract boolean isLeaf();

    public abstract TreeTask failureTask();



}
