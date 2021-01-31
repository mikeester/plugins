package net.runelite.client.plugins.treetemplate.tasks;


import net.runelite.client.plugins.treetemplate.BranchTask;
import net.runelite.client.plugins.treetemplate.TreeTemplatePlugin;
import net.runelite.client.plugins.treetemplate.TreeTask;

public class IsTimeout extends BranchTask
{

    public IsTimeout() {
    }

    @Override
    public boolean validate() {
        return TreeTemplatePlugin.timeout > 0 || TreeTemplatePlugin.conditionTimeout != null;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(HandleTimeout.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(IsMoving.class);
    }

}
