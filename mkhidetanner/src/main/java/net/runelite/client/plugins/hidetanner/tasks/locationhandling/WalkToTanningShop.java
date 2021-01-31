package net.runelite.client.plugins.hidetanner.tasks.locationhandling;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.WallObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.WallObjectQuery;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;

import java.util.Random;

/**
 * NOTES:
 *
 */
@Slf4j
public class WalkToTanningShop extends LeafTask {

    public WalkToTanningShop() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {

        Player player = client.getLocalPlayer();
        if (player == null)
        {
            return;
        }

        NPC tanner = npcUtils.findNearestNpc("Ellis");
        if (tanner == null)
        {
            // Walk to midway point
            log.info("[MK] Walking towrds tan shop");
            status = "Walking to tanner";
            // new Coordinate(3276, 3179, 0), new Coordinate(3280, 3183, 0));
            WorldArea area = new WorldArea(new WorldPoint(3276, 3179, 0), new WorldPoint(3280, 3183, 0));
            WorldPoint wp = area.toWorldPointList().get(new Random().nextInt(area.toWorldPointList().size()));
            walk.sceneWalk(wp, 1, sleepDelay());
        }
        else
        {
            WallObject tanRoomDoorClosed = new WallObjectQuery()
                    .idEquals(MKHideTannerData.DOOR_CLOSED_ID)
                    .atWorldLocation(MKHideTannerData.DOOR_CLOSED_TILE)
                    .result(client)
                    .first();

            if (tanRoomDoorClosed != null)
            {
                // Open the door
                status = "Opening door";
                log.info("[MK] opening tan shop door");
                utils.doTileObjectActionClientTick(tanRoomDoorClosed, MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId(), tickDelay());
            }

        }
    }
}
