package net.runelite.client.plugins.hidetanner.tasks.locationhandling;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Player;
import net.runelite.api.WallObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.WallObjectQuery;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

import java.util.Random;

@Slf4j
public class WalkToBank extends LeafTask {

    public WalkToBank() { }

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
            WallObject tanRoomDoorClosed = new WallObjectQuery()
                    .idEquals(MKHideTannerData.DOOR_CLOSED_ID)
                    .atWorldLocation(MKHideTannerData.DOOR_CLOSED_TILE)
                    .result(client)
                    .first();


            if (tanRoomDoorClosed != null && MKHideTannerData.TAN_ROOM.intersectsWith(player.getWorldArea()))
            {
                // Stuck inside tan room with door closed so open it
                status = "Open door";
                utils.doTileObjectActionMsTime(tanRoomDoorClosed, MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
                MKHideTannerPlugin.timeout = calc.getRandomIntBetweenRange(1, 3);
            }
            else
            {
                // Walk to bank
                status = "Walk to bank";
                WorldPoint wp = MKHideTannerData.BANK_BOOTH_TILES.toWorldPointList().get(new Random().nextInt(MKHideTannerData.BANK_BOOTH_TILES.toWorldPointList().size()));
                walk.sceneWalk(wp, 0, sleepDelay());
            }
        }
    }
}
