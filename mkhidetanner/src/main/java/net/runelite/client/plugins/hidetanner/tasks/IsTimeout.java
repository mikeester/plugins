package net.runelite.client.plugins.hidetanner.tasks;


import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;
import net.runelite.client.plugins.hidetanner.TreeTask;

public class IsTimeout extends BranchTask {

    public IsTimeout() {
    }

    @Override
    public boolean validate() {
        return MKHideTannerPlugin.timeout > 0;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(Timeout.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(IsMoving.class);
    }

}
