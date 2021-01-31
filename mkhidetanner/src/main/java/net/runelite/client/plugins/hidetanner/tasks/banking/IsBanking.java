package net.runelite.client.plugins.hidetanner.tasks.banking;


import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.itemhandling.HasCoins;
import net.runelite.client.plugins.hidetanner.tasks.locationhandling.AtBank;
import net.runelite.client.plugins.hidetanner.tasks.npchandling.IsTanInterfaceOpen;

public class IsBanking extends BranchTask {

    public IsBanking() {
    }

    @Override
    public boolean validate() {
        return MKHideTannerPlugin.isBanking;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(AtBank.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(IsTanInterfaceOpen.class);
    }

}
