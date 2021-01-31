package net.runelite.client.plugins.hidetanner.tasks.itemhandling;


import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.CloseBank;
import net.runelite.client.plugins.hidetanner.tasks.banking.OpenBank;
import net.runelite.client.plugins.hidetanner.tasks.banking.SetNotBanking;
import net.runelite.client.plugins.hidetanner.tasks.banking.WithdrawDragonhide;

public class HasDragonhideBanking extends BranchTask {

    public HasDragonhideBanking() {


    }



    private CloseBank closeBank;
    private WithdrawDragonhide withdrawDragonhide;
    private SetNotBanking setNotBanking;
    private OpenBank openBank;


    @Override
    public boolean validate() {

        int hideInInventory = inventory.getItemCount(MKHideTannerPlugin.hideID, false);

        if (bank.isOpen()) {

            Widget hide = bank.getBankItemWidget(MKHideTannerPlugin.hideID);
            int hideInBank = 0;

            if (hide != null) {
                hideInBank = hide.getItemQuantity();
            }

            if (hideInInventory > 0) {

                // We have enough if we have some hides to tan and no more to withdraw from bank
                if (hideInBank == 0) {
                    return true;
                }

                // If we have room for more hide we should withdraw more
                return inventory.isFull();

            } else {

                return false;

            }

        } else {

            return hideInInventory > 0;

        }
    }

    @Override
    public TreeTask successTask() {
        if (bank.isOpen()) {
            return injector.getInstance(CloseBank.class);
        } else {
            return injector.getInstance(SetNotBanking.class);
        }
    }

    @Override
    public TreeTask failureTask() {
        if (bank.isOpen()) {
            return injector.getInstance(WithdrawDragonhide.class);
        } else {
            return injector.getInstance(OpenBank.class);
        }
    }

}
