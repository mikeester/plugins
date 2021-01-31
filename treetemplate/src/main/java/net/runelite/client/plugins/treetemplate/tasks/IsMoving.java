package net.runelite.client.plugins.treetemplate.tasks;


import net.runelite.client.plugins.treetemplate.BranchTask;
import net.runelite.client.plugins.treetemplate.TreeTask;

public class IsMoving extends BranchTask
{

    public IsMoving() {
    }

    @Override
    public boolean validate() {
        // return playerUtils.isMoving(TreeTemplatePlugin.beforeLoc);
        return false;
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(SetMoveTimeout.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(LeafA.class);
    }

}
