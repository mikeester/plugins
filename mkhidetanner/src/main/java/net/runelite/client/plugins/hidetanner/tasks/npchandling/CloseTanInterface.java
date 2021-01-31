package net.runelite.client.plugins.hidetanner.tasks.npchandling;

import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.hidetanner.LeafTask;

public class CloseTanInterface extends LeafTask {

    public CloseTanInterface() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        Player player = client.getLocalPlayer();
        Widget tanInterface = client.getWidget(324, 96);
        if (tanInterface != null && !tanInterface.isHidden())
        {
            Widget closeBtn = client.getWidget(324, 89);
            if (closeBtn != null && !closeBtn.isHidden())
            {

                status = "Close tan interface";
                MenuEntry entry = new MenuEntry("Close", "", 0, MenuOpcode.WIDGET_TYPE_3.getId(), 0, 21233754, false);
                utils.doActionMsTime(entry, closeBtn.getBounds(), sleepDelay());

            }
        }
    }
}
