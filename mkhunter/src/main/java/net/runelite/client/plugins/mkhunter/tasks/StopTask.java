package net.runelite.client.plugins.mkhunter.tasks;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;

public class StopTask extends Task
{

    @Override
    public boolean validate()
    {
        return MKHunterPlugin.shouldStop;
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
        MKHunterPlugin.startBot = false;
        MKHunterPlugin.shouldStop = false;
    }

}
