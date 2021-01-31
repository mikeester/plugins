package net.runelite.client.plugins.hidetanner.tasks;


import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

/**
 * NOTES:
 *
 */
public class SetMoveTimeout extends LeafTask {

    public SetMoveTimeout() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            playerUtils.handleRun(20, 30);
            MKHideTannerPlugin.timeout = tickDelay();
        }
    }
}