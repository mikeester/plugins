package net.runelite.client.plugins.hidetanner.tasks.itemhandling;


import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.banking.DepositUnwanted;
import net.runelite.client.plugins.hidetanner.tasks.banking.OpenBank;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HasItemsToDeposit extends BranchTask {

    public HasItemsToDeposit() {

    }




    @Override
    public boolean validate() {
        return inventory.containsExcept(Arrays.stream(MKHideTannerData.WANTED_ITEM_IDS).boxed().collect(Collectors.toList()));
    }

    @Override
    public TreeTask successTask() {
        if (bank.isOpen()) {
            return injector.getInstance(DepositUnwanted.class);
        } else {
            return injector.getInstance(OpenBank.class);
        }
    }

    @Override
    public TreeTask failureTask() {
        if (bank.isOpen()) {
            return injector.getInstance(HasCoinsBanking.class);
        } else {
            return injector.getInstance(HasDragonhideBanking.class);
        }
    }

}
