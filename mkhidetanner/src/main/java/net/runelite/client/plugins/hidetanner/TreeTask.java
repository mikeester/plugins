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

@Slf4j
public abstract class TreeTask {

    public TreeTask() {}

    @Inject
    public Injector injector;

    @Inject
    public Client client;

    @Inject
    public MKHideTannerConfig config;

    @Inject
    public MKHideTannerPlugin plugin;

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

    public static String status;

    public long sleepDelay()
    {
        MKHideTannerPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
        return MKHideTannerPlugin.sleepLength;
    }

    public int tickDelay()
    {
        MKHideTannerPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
        return MKHideTannerPlugin.tickLength;
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
