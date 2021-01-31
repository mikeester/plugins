package net.runelite.client.plugins.hidetanner.tasks.locationhandling;


import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.npchandling.IsTanInterfaceOpen;

public class AtTanningShop extends BranchTask {

    public AtTanningShop() {

    }

    @Override
    public boolean validate() {

        Player local = client.getLocalPlayer();
        if (local != null) {
            WorldPoint current = local.getWorldLocation();
            return MKHideTannerData.TAN_ROOM.toWorldPointList().contains(local.getWorldLocation()) || MKHideTannerData.TANNING_TILE.equals(current);
        }
        return false;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(IsTanInterfaceOpen.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(WalkToTanningShop.class);
    }

}