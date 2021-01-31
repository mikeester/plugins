package net.runelite.client.plugins.mkhunter.tasks.monkeys;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.MKHunterData;
import net.runelite.client.plugins.mkhunter.Task;

import java.util.Random;

@Slf4j
public class WalkToBoulderTask extends Task
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

        // Boulder not loaded
        GameObject boulder = object.getGameObjectAtWorldPoint(MKHunterData.BOULDER_TILE);
        return boulder == null;
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        status = "Walk to boulder";
        WorldArea area = new WorldArea(new WorldPoint(2912, 9127, 0), new WorldPoint(2916, 9129, 0));
        WorldPoint wp = area.toWorldPointList().get(new Random().nextInt(area.toWorldPointList().size()));
        walking.sceneWalk(wp, 1, sleepDelay());
    }

}

