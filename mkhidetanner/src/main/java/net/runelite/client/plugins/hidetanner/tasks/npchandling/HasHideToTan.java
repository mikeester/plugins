package net.runelite.client.plugins.hidetanner.tasks.npchandling;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.SetBanking;

@Slf4j
public class HasHideToTan extends BranchTask {

    public HasHideToTan() { }


    @Override
    public boolean validate() {
        return inventory.containsItem(MKHideTannerPlugin.hideID) && inventory.getItemCount(ItemID.COINS_995, true) >= 20;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(CanTradeTanner.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(SetBanking.class);
    }

}
