package net.runelite.client.plugins.mkhunter.tasks.falconry;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

import java.util.Collections;

@Slf4j
public class GetFalconTask extends Task
{

    @Override
    public boolean validate()
    {
        if (equipmentUtils.isItemEquipped(Collections.singletonList(ItemID.FALCONERS_GLOVE_10024)))
        {
            if (plugin.lostFalcon)
            {
                plugin.lostFalcon = false;
            }
            return false;
        }

        if (plugin.lostFalcon)
        {
            return true;
        }

        if (plugin.falconIsFlyingToTarget())
        {
            return false;
        }

        if (client.getHintArrowNpc() != null)
        {
            if (plugin.lostFalcon)
            {
                plugin.lostFalcon = false;
            }
            return false;
        }

        if (inventory.getItemCount(ItemID.COINS_995, true) < 500)
        {
            MKHunterPlugin.shouldStop = true;
            log.info("Not enough GP to get bird, stopping");
            return false;
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
        status = "Get falcon";
        // Continue dialogs

        // NPC
        String npcText = dialogUtils.getNpcText();
        if (npcText != null)
        {
            if (npcText.contains("help you at all") || npcText.contains("I suppose I could let you try") || npcText.contains("I have some tamer birds") || npcText.contains("request a small fee"))
            {
                dialogUtils.continueNpcDialog(sleepDelay());
                MKHunterPlugin.timeout = tickDelay();
                return;
            }
            else if (npcText.contains("aren't domesticated you know") || npcText.contains("want me to fetch him"))
            {
                // Lost falcon but has glove
                dialogUtils.continueNpcDialog(sleepDelay());
                MKHunterPlugin.timeout = tickDelay();
                return;
            }
        }

        // Player
        String playerText = dialogUtils.getPlayerText();
        if (playerText != null)
        {
            if (playerText.contains("Hello there") || playerText.contains("have a go with your bird") || playerText.contains("seems reasonable"))
            {
                dialogUtils.continuePlayerDialog(sleepDelay());
                MKHunterPlugin.timeout = tickDelay();
                return;
            }
            else if (playerText.contains("your falcon ran away") || playerText.contains("Yes, please"))
            {
                // Lost falcon but has glove
                dialogUtils.continuePlayerDialog(sleepDelay());
                MKHunterPlugin.timeout = tickDelay();
                return;
            }
        }

        // Options
        String optionThree = dialogUtils.getOptionText(3);
        if (optionThree != null && optionThree.contains("have a go with your bird"))
        {
            log.info("Selecting option 3");
            dialogUtils.selectDialogOption(3, sleepDelay());
            MKHunterPlugin.timeout = tickDelay();
            return;
        }

        String optionOne = dialogUtils.getOptionText(1);
        if (optionOne != null && (optionOne.contains("seems reasonable") || optionOne.contains("Yes, please")))
        {
            log.info("Selecting option 1");
            dialogUtils.selectDialogOption(1, sleepDelay());
            MKHunterPlugin.timeout = tickDelay();
            return;
        }

        // No dialog actions, speak to Matthias
        NPC matthias = npcUtils.findNearestNpc(NpcID.MATTHIAS_1341, NpcID.MATTHIAS);
        if (matthias != null)
        {
            MenuEntry entry = new MenuEntry("Talk-to", "<col=ffff00>Matthias", matthias.getIndex(), MenuOpcode.NPC_FIRST_OPTION.getId(), 0, 0, false);
            utils.doActionMsTime(entry, matthias.getConvexHull().getBounds(), sleepDelay());
            MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> dialogUtils.getPlayerText() != null, () -> playerUtils.isMoving(), 5);
            MKHunterPlugin.timeout = tickDelay();
        }
        else
        {
            // Walk toward Matthias area
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
                    log.info("Walking to matthias");
                    walking.sceneWalk(new WorldPoint(2375, 3607, 0), 5, sleepDelay());
                    MKHunterPlugin.timeout = tickDelay();
                }
            }
        }

    }

}

