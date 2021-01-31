package net.runelite.client.plugins.mkhunter.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;

@Slf4j
public class CloseLevelUpDialogTask extends Task
{

    @Override
    public boolean validate()
    {
        Widget levelUpWidget = client.getWidget(WidgetInfo.LEVEL_UP);

        return levelUpWidget != null && !levelUpWidget.isHidden();
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Widget levelUpWidget = client.getWidget(WidgetInfo.LEVEL_UP);

        if (levelUpWidget != null && !levelUpWidget.isHidden())
        {
            Player local = client.getLocalPlayer();
            if (local == null)
            {
                return;
            }

            log.info("Walking on spot to close level up dialog");
            status = "Exit level up";
            walking.sceneWalk(local.getWorldLocation(), 0, sleepDelay());
            MKHunterPlugin.timeout = tickDelay();
        }
    }

}

