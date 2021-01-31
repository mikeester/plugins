package net.runelite.client.plugins.hidetanner.tasks.npchandling;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.WallObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.WallObjectQuery;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.SetBanking;
import net.runelite.client.plugins.hidetanner.tasks.locationhandling.WalkToTanningShop;

import java.util.Random;

@Slf4j
public class CanTradeTanner extends BranchTask {

    public CanTradeTanner() { }


    @Override
    public boolean validate() {
        Player local = client.getLocalPlayer();

        if (local == null)
        {
            return false;
        }

        NPC tanner = npcUtils.findNearestNpc("Ellis");
        if (tanner == null)
        {
            return false;
        }

        WallObject tanRoomDoorClosed = new WallObjectQuery()
                .idEquals(MKHideTannerData.DOOR_CLOSED_ID)
                .atWorldLocation(MKHideTannerData.DOOR_CLOSED_TILE)
                .result(client)
                .first();

        if (tanRoomDoorClosed != null)
        {
            return MKHideTannerData.TAN_ROOM.intersectsWith(local.getWorldArea());
        }

        return true;

    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(TradeTanner.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(WalkToTanningShop.class);
    }

}
