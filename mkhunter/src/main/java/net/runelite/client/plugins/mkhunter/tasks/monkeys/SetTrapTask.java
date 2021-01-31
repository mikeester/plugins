package net.runelite.client.plugins.mkhunter.tasks.monkeys;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuOpcode;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.plugins.mkhunter.MKHunterData;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

@Slf4j
public class SetTrapTask extends Task
{

    @Override
    public boolean validate()
    {
        if (!plugin.canSetupTrap())
        {
            return false;
        }

        return inventory.containsItem(MKHunterData.BANANA);

    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        GameObject idleBoulder = new GameObjectQuery()
                .idEquals(MKHunterData.EMPTY_BOULDER)
                .atWorldLocation(MKHunterData.BOULDER_TILE)
                .result(client)
                .first();

        if (idleBoulder != null)
        {
            status = "Set trap";
            // MenuEntry entry = new MenuEntry("Check", "<col=ffff>Large boulder", caughtTrap.getId(), MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId(), caughtTrap.getSceneMinLocation().getX(), caughtTrap.getSceneMinLocation().getY(), false);
            utils.doGameObjectActionMsTime(idleBoulder, MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
            int before = inventory.getItemCount(MKHunterData.BANANA, false);
            MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> inventory.getItemCount(MKHunterData.BANANA, false) < before, 5, 10, 7, 1);
            MKHunterPlugin.timeout = calc.getRandomIntBetweenRange(1, 4);
        } }

}

