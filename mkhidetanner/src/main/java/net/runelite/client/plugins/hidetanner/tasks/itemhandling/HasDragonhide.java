package net.runelite.client.plugins.hidetanner.tasks.itemhandling;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.SetBanking;
import net.runelite.client.plugins.hidetanner.tasks.locationhandling.AtTanningShop;
import net.runelite.client.plugins.hidetanner.tasks.npchandling.IsTanInterfaceOpen;

@Slf4j
public class HasDragonhide extends BranchTask {

    public HasDragonhide() {
    }

    @Override
    public boolean validate() {
        return inventory.containsItem(MKHideTannerPlugin.hideID);
    } // TODO : config

    @Override
    public TreeTask successTask() {
        return injector.getInstance(IsTanInterfaceOpen.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(SetBanking.class);
    }

}
