package net.runelite.client.plugins.treetemplate.tasks;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.treetemplate.LeafTask;
import net.runelite.client.plugins.treetemplate.TreeTemplatePlugin;

public class Stop extends LeafTask
{

    public Stop() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        status = "Stopping...";
        TreeTemplatePlugin.startBot = false;
        TreeTemplatePlugin.shouldStop = false;
    }
}
