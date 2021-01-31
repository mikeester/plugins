package net.runelite.client.plugins.hidetanner.tasks.banking;


import net.runelite.api.GameObject;
import net.runelite.api.MenuOpcode;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

/**
 * NOTES:
 *
 */
public class OpenBank extends LeafTask {

    public OpenBank() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        GameObject bank = object.findNearestBank();

        if (bank != null)
        {
            status = "Open bank";
            utils.doGameObjectActionMsTime(bank, MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId(), sleepDelay());
            MKHideTannerPlugin.timeout = calc.getRandomIntBetweenRange(2, 3);
        }
    }
}