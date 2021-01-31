package net.runelite.client.plugins.hidetanner.tasks;


import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.IsBanking;
import net.runelite.client.plugins.hidetanner.tasks.itemhandling.HasCoins;
import net.runelite.client.plugins.hidetanner.tasks.locationhandling.AtBank;

public class IsMoving extends BranchTask {

    public IsMoving() {
    }

    private AtBank atBank;
    private HasCoins hasCoins;

    @Override
    public boolean validate() {
        // return false;
        return playerUtils.isMoving(MKHideTannerPlugin.beforeLoc);
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(SetMoveTimeout.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(IsBanking.class);
    }

}
