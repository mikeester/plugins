package net.runelite.client.plugins.hidetanner.tasks.itemhandling;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.SetBanking;
import net.runelite.client.plugins.hidetanner.tasks.npchandling.CloseTanInterface;
import net.runelite.client.plugins.hidetanner.tasks.npchandling.TanHide;

@Slf4j
public class CanTanHide extends BranchTask {

    public CanTanHide() {
    }

    @Override
    public boolean validate() {
        return inventory.containsItem(MKHideTannerPlugin.hideID) && inventory.getItemCount(ItemID.COINS_995, true) >= 20;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(TanHide.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(CloseTanInterface.class);
    }

}
