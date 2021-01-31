package net.runelite.client.plugins.treetemplate.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.treetemplate.LeafTask;

@Slf4j
public class LeafA extends LeafTask {

    public LeafA() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        // log.info("Leaf A Executing!");
    }

}
