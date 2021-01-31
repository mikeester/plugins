package net.runelite.client.plugins.hidetanner.tasks;


import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

/**
 * NOTES:
 *
 */
public class Timeout extends LeafTask {

    public Timeout() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        MKHideTannerPlugin.timeout--;
    }
}