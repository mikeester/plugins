package net.runelite.client.plugins.mkhunter.tasks.monkeys;

import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.mkhunter.MKHunterData;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

public class CastBonesToBananasTask extends Task
{

    @Override
    public boolean validate()
    {

        // Don't convert while trap is set up
        if (plugin.hasTrapSetup())
        {
            return false;
        }

        // Has bananas to keep hunting
        if (inventory.containsItem(MKHunterData.BANANA))
        {
            return false;
        }

        int bonesQuantity = inventory.getItemCount(MKHunterData.BONES, false);

        // No bones to convert
        if (bonesQuantity <= 0)
        {
            return false;
        }

        // No tab, can't cast
        if (!inventory.containsItem(MKHunterData.TAB))
        {
            return false;
        }

        // Need a full inv before casting to maximise bones
        return inventory.isFull();
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        if (!inventory.isOpen())
        {
            inventory.openInventory();
        }
        else
        {
            WidgetItem tab = inventory.getWidgetItem(MKHunterData.TAB);
            if (tab != null)
            {
                status = "Break B2B tab";
                MenuEntry entry = new MenuEntry("Break", "Break", tab.getId(), MenuOpcode.ITEM_FIRST_OPTION.getId(), tab.getIndex(), WidgetInfo.INVENTORY.getId(), false);
                utils.doActionMsTime(entry, tab.getCanvasBounds(), sleepDelay());
                MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> !inventory.containsItem(MKHunterData.BONES), 5);
                MKHunterPlugin.timeout = 3;
            }
        }
    }

}

