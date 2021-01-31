package net.runelite.client.plugins.template.tasks;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.template.Task;
import net.runelite.client.plugins.template.TemplatePlugin;

public class StopTask extends Task
{

    @Override
    public boolean validate()
    {
        return TemplatePlugin.shouldStop;
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        status = "Stopping...";
        plugin.resetVals();
    }
}
