package net.runelite.client.plugins.treetemplate;

import net.runelite.api.events.GameTick;

public abstract class BranchTask extends TreeTask {

    public BranchTask() { }

    public final boolean isLeaf() {
        return false;
    }

    public final void onGameTick(GameTick event)
    {
        return;
    }

}
