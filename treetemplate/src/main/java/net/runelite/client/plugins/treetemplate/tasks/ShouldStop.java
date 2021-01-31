package net.runelite.client.plugins.treetemplate.tasks;


import net.runelite.client.plugins.treetemplate.BranchTask;
import net.runelite.client.plugins.treetemplate.TreeTemplatePlugin;
import net.runelite.client.plugins.treetemplate.TreeTask;

public class ShouldStop extends BranchTask
{

    public ShouldStop() {
    }

    @Override
    public boolean validate() {
        return TreeTemplatePlugin.shouldStop;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(Stop.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(IsTimeout.class);
    }

}
