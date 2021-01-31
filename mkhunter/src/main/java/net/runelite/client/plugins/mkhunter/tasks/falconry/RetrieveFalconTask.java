package net.runelite.client.plugins.mkhunter.tasks.falconry;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

import java.util.Collections;

@Slf4j
public class RetrieveFalconTask extends Task
{

    @Override
    public boolean validate()
    {
        if (!equipmentUtils.isItemEquipped(Collections.singleton(ItemID.FALCONERS_GLOVE)))
        {
            return false;
        }

        NPC hintArrowNPC = client.getHintArrowNpc();
        if (hintArrowNPC == null)
        {
            return false;
        }

        return hintArrowNPC.getId() == config.kebbitChoice().falconId && inventory.getEmptySlots() > 1;

    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        status = "Loot falcon";
        NPC falcon = client.getHintArrowNpc();
        if (falcon == null)
        {
            return;
        }

        MenuEntry entry = new MenuEntry("Retrieve", "<col=ffff00>Gyr Falcon", falcon.getIndex(), MenuOpcode.NPC_FIRST_OPTION.getId(), 0, 0, false);
        int before = client.getSkillExperience(Skill.HUNTER);
        utils.doActionMsTime(entry, falcon.getConvexHull().getBounds(), sleepDelay());
        MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> client.getSkillExperience(Skill.HUNTER) > before, () -> playerUtils.isMoving(), 7);
        MKHunterPlugin.timeout = tickDelay();
    }

}

