package net.runelite.client.plugins.hidetanner.tasks.banking;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

/**
 * NOTES:
 *
 */
public class CloseBank extends LeafTask {

    public CloseBank() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {

        status = "Close bank";
        bank.close();
        MKHideTannerPlugin.timeout = tickDelay();

    }
}
