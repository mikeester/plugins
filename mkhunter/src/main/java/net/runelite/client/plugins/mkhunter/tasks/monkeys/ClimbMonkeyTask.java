package net.runelite.client.plugins.mkhunter.tasks.monkeys;

import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.MKHunterData;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

public class ClimbMonkeyTask extends Task
{

    @Override
    public boolean validate()
    {
        // Null local player
        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return false;
        }

        // Already on monkey
        if (local.getPoseAnimation() == 7235 || local.getPoseAnimation() == 7234)
        {
            return false;
        }

        // Monkey to climb
        return npcUtils.findNearestNpc(MKHunterData.GORILLA) != null;
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        NPC gorilla = npcUtils.findNearestNpc(MKHunterData.GORILLA);
        if (gorilla != null)
        {
            status = "Climb monkey";
            // MenuEntry entry = new MenuEntry("Climb-on", "<col=ffff00>Stunted demonic gorilla<col=ff0000>  (level-142)", 24040, 9, 0, 0, false);
            utils.doNpcActionMsTime(gorilla, MenuOpcode.NPC_FIRST_OPTION.getId(), sleepDelay());
            MKHunterPlugin.conditionTimeout = new TimeoutUntil(
                    () -> {
                        Player local = client.getLocalPlayer();
                        if (local == null)
                        {
                            return false;
                        }

                        // On monkey
                        if (local.getPoseAnimation() == 7235 || local.getPoseAnimation() == 7234)
                        {
                            return true;
                        }

                        return false;
                    },
                    () -> playerUtils.isMoving(MKHunterPlugin.beforeLoc),
                    5, 10, 7, 1);
        }
    }

}

