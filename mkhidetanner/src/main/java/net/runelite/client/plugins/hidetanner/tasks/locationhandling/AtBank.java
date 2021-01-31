package net.runelite.client.plugins.hidetanner.tasks.locationhandling;


import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.IsBankOpen;

public class AtBank extends BranchTask {

    public AtBank() {

    }


    @Override
    public boolean validate() {
        Player local = client.getLocalPlayer();
        if (local != null) {
            WorldPoint current = local.getWorldLocation();
            return (MKHideTannerData.BANK_AREA.toWorldPointList().contains(local.getWorldLocation()) || MKHideTannerData.BANKING_TILE.equals(current));
        }
        return false;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(IsBankOpen.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(WalkToBank.class);
    }

}
