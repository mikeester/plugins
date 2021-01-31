package net.runelite.client.plugins.mkhunter.tasks.monkeys;

import net.runelite.api.GameObject;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.plugins.mkhunter.MKHunterData;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

public class CheckTrapTask extends Task
{

    @Override
    public boolean validate()
    {
        // No trap to check
        if (!plugin.hasTrapCaught())
        {
            return false;
        }

        // Need 1 space for tail
        return !inventory.isFull();
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        GameObject caughtTrap = new GameObjectQuery()
                .idEquals(MKHunterData.CAUGHT_EAST, MKHunterData.CAUGHT_SOUTH)
                .atWorldLocation(MKHunterData.BOULDER_TILE)
                .result(client)
                .first();

        if (caughtTrap != null)
        {
            status = "Check trap";
            // MenuEntry entry = new MenuEntry("Check", "<col=ffff>Large boulder", caughtTrap.getId(), MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId(), caughtTrap.getSceneMinLocation().getX(), caughtTrap.getSceneMinLocation().getY(), false);
            utils.doGameObjectActionMsTime(caughtTrap, MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
            int before = client.getSkillExperience(Skill.HUNTER);
            MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> client.getSkillExperience(Skill.HUNTER) > before, 5, 10, 7, 1);
        }
    }

}

