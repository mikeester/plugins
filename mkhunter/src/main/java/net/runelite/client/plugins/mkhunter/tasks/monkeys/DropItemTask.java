package net.runelite.client.plugins.mkhunter.tasks.monkeys;

import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.mkhunter.MKHunterData;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;

import java.util.Collections;

public class DropItemTask extends Task
{

    @Override
    public boolean validate()
    {
        // Has damaged tail to drop
        if (!inventory.containsItem(MKHunterData.DAMAGED_TAIL))
        {
            // If no tail, we need space if our inv is full and we need to collect a trap
            return plugin.hasTrapCaught() && inventory.isFull();
        }

        return true;
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
            WidgetItem damagedTail = inventory.getWidgetItem(MKHunterData.DAMAGED_TAIL);
            if (damagedTail != null)
            {
                status = "Drop item";
                inventory.dropItems(Collections.singletonList(damagedTail.getId()), true, config.sleepMin(), config.sleepMax());
                MKHunterPlugin.timeout = calc.getRandomIntBetweenRange(1, 2);
            }
            else
            {
                if (inventory.isFull())
                {

                    WidgetItem bones = inventory.getWidgetItem(MKHunterData.BONES);
                    if (bones != null)
                    {
                        status = "Drop item";
                        inventory.dropItems(Collections.singletonList(bones.getId()), false, config.sleepMin(), config.sleepMax());
                        MKHunterPlugin.timeout = 1;
                    }
                    else
                    {
                        WidgetItem banana = inventory.getWidgetItem(MKHunterData.BANANA);
                        if (banana != null)
                        {
                            status = "Drop item";
                            inventory.dropItems(Collections.singletonList(banana.getId()), false, config.sleepMin(), config.sleepMax());
                            MKHunterPlugin.timeout = 1;
                        }
                    }
                }
            }
        }
        // WidgetItem toDrop = plugin.itemsToDrop.get(0);
        // //new MenuEntry(option=Drop, target=<col=ff9040>Molten glass, identifier=1775, opcode=37, param0=5, param1=9764864, forceLeftClick=false)
        // if (toDrop != null) {
        //     // inventory.dropItem(toDrop);
        //     inventory.dropItems(Arrays.asList(toDrop.getId()), false, config.sleepMin(), config.sleepMax());
        //     // TODO better dropping to make sure we only attempt to drop existing items
        //     plugin.itemsToDrop.remove(0);
        // }


    }

}

