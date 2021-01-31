package net.runelite.client.plugins.hidetanner.tasks;

import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

public class Stop extends LeafTask
{

    public Stop() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        status = "Stopping...";
        MKHideTannerPlugin.startBot = false;
        MKHideTannerPlugin.shouldStop = false;
    }
}
