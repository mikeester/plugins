package net.runelite.client.plugins.hidetanner.tasks.banking;


import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.itemhandling.HasCoinsBanking;
import net.runelite.client.plugins.hidetanner.tasks.itemhandling.HasItemsToDeposit;

public class IsBankOpen extends BranchTask {

    public IsBankOpen() {
    }

    @Override
    public boolean validate() {
        return bank.isOpen();
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(HasItemsToDeposit.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(HasCoinsBanking.class);
    }

}
