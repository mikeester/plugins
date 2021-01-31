package net.runelite.client.plugins.treetemplate;

public abstract class LeafTask extends TreeTask
{

    public LeafTask() { }

    public final boolean isLeaf() {
        return true;
    }

    public final boolean validate() {
        return true;
    }

    public final TreeTask successTask() {
        return null;
    }

    public final TreeTask failureTask() {
        return null;
    }

}
