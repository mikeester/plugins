package net.runelite.client.plugins.mkhunter.tasks.falconry;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;
import net.runelite.client.plugins.mkutils.MKUtils;

import java.util.List;

@Slf4j
public class DropLootTask extends Task
{

    @Override
    public boolean validate()
    {
        return inventory.getEmptySlots() < 2 && inventory.containsItem(List.of(ItemID.BONES, config.kebbitChoice().furId));
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        status = "Drop item";

        if (MKUtils.iterating)
        {
            return;
        }

        inventory.dropItems(List.of(ItemID.BONES, config.kebbitChoice().furId), true, config.sleepMin(), config.sleepMax());
        MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> !inventory.containsItem(List.of(ItemID.BONES, config.kebbitChoice().furId)), 10);
        MKHunterPlugin.timeout = tickDelay();
    }

}

