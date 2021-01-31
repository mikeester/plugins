package net.runelite.client.plugins.mkhunter.tasks.falconry;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

import java.util.Collections;

@Slf4j
public class CatchKebbitTask extends Task
{

    @Override
    public boolean validate()
    {
        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return false;
        }

        if (!equipmentUtils.isItemEquipped(Collections.singleton(ItemID.FALCONERS_GLOVE_10024)))
        {
            return false;
        }

        if (client.getHintArrowNpc() != null)
        {
            return false;
        }

        if (playerUtils.isAnimating())
        {
            return false;
        }

        // Has falcon
        return local.getPoseAnimation() >= 5160 && local.getPoseAnimation() <= 5170;
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        if (client.getBoostedSkillLevel(Skill.HUNTER) < config.kebbitChoice().hunterLvl)
        {
            MKHunterPlugin.shouldStop = true;
            log.info("[MK] Hunter level is too low to catch " + config.kebbitChoice() + " kebbit ( " + config.kebbitChoice().hunterLvl + "), stopping...");
            return;
        }

        NPC kebbit = npcUtils.findNearestNpc(config.kebbitChoice().npcId);
        if (kebbit != null)
        {
            status = "Catch kebbit";
            MenuEntry entry = new MenuEntry("Catch", "", kebbit.getIndex(), MenuOpcode.NPC_FIRST_OPTION.getId(), 0, 0, false);
            utils.doActionMsTime(entry, kebbit.getConvexHull().getBounds(), sleepDelay());
            MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> {
                if (client.getHintArrowNpc() != null)
                {
                    return true;
                }

                // It flew back (failed)
                Player local = client.getLocalPlayer();
                if (local != null && !playerUtils.isMoving() && local.getPoseAnimation() == 5160)
                {
                    return true;
                }

                return false;

            }, () -> playerUtils.isMoving(), 6, 10, 7, 1);
            MKHunterPlugin.timeout = tickDelay();
        }
        else
        {
            // Walk toward kebbit area
            Player local = client.getLocalPlayer();
            if (local == null)
            {
                return;
            }

            WorldArea localArea = local.getWorldArea();
            if (localArea != null)
            {
                if (new WorldArea(new WorldPoint(2363, 3572, 0), new WorldPoint(2395, 3621, 0)).intersectsWith(localArea))
                {
                    status = "Walk to kebbits";
                    walking.sceneWalk(new WorldPoint(2377, 3587, 0), 5, sleepDelay());
                    MKHunterPlugin.timeout = tickDelay();
                }
            }
        }
    }

}

