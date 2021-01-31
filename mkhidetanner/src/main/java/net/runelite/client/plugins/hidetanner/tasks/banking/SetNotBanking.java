package net.runelite.client.plugins.hidetanner.tasks.banking;


import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

/**
 * NOTES:
 *
 */
public class SetNotBanking extends LeafTask {

    public SetNotBanking() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        MKHideTannerPlugin.isBanking = false;
    }
}
