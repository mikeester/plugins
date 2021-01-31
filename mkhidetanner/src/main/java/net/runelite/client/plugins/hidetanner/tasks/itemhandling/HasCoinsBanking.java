package net.runelite.client.plugins.hidetanner.tasks.itemhandling;


import net.runelite.api.ItemID;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.OpenBank;
import net.runelite.client.plugins.hidetanner.tasks.banking.WithdrawCoins;

public class HasCoinsBanking extends BranchTask {

    public HasCoinsBanking() {
    }



    @Override
    public boolean validate() {
        return inventory.getItemCount(ItemID.COINS_995, true) >= 540; // Min cost of 20gp*27 hide
    }

    @Override
    public TreeTask successTask() {
        if (bank.isOpen()) {
            return injector.getInstance(HasDragonhideBanking.class);
        } else {
            return injector.getInstance(HasItemsToDeposit.class);
        }
    }

    @Override
    public TreeTask failureTask() {
        if (bank.isOpen()) {
            return injector.getInstance(WithdrawCoins.class);
        } else {
            return injector.getInstance(OpenBank.class);
        }
    }

}