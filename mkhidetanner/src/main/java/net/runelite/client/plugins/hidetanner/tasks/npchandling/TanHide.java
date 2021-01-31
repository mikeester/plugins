package net.runelite.client.plugins.hidetanner.tasks.npchandling;


import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

public class TanHide extends LeafTask {

    public TanHide() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        //public static final int TAN_INTERFACE_CONTAINER = 324,
        //             SOFT_TAN_ALL_INDEX = 124, HARD_TAN_ALL_INDEX = 125,
        //             GREEN_TAN_ALL_INDEX = 128, BLUE_TAN_ALL_INDEX = 129, RED_TAN_ALL_INDEX = 130, BLACK_TAN_ALL_INDEX = 131;

        Widget tanHideWidget = client.getWidget(324, MKHideTannerPlugin.hideInterfaceChildID);
        if (tanHideWidget != null && !tanHideWidget.isHidden())
        {

            status = "Tan hide";
            MenuEntry entry = new MenuEntry("Tan <col=ff7000>All", "", 0, MenuOpcode.WIDGET_TYPE_1.getId(), 0, tanHideWidget.getId(), false);
            utils.doActionMsTime(entry, tanHideWidget.getBounds(), sleepDelay());

        }
    }
}
