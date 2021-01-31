package net.runelite.client.plugins.hidetanner.tasks;


import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.IsBanking;
import net.runelite.client.plugins.hidetanner.tasks.itemhandling.HasCoins;
import net.runelite.client.plugins.hidetanner.tasks.locationhandling.AtBank;

public class ShouldStop extends BranchTask {

    public ShouldStop() {
    }

    @Override
    public boolean validate() {
        return MKHideTannerPlugin.shouldStop;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(Stop.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(IsTimeout.class);
    }

}
