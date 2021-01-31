package net.runelite.client.plugins.hidetanner.tasks.itemhandling;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.SetBanking;

@Slf4j
public class HasCoins extends BranchTask {

    public HasCoins() {
    }

    @Override
    public boolean validate() {
        return inventory.getItemCount(ItemID.COINS_995, true) >= 540; // Min cost of 20gp*27 hide
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(HasDragonhide.class);
    }

    @Override
    public TreeTask failureTask() {
        log.info("HasCoins: No coins, set banking");
        return injector.getInstance(SetBanking.class);
    }

}
