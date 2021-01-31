package net.runelite.client.plugins.mkhunter.tasks.monkeys;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.TileQuery;
import net.runelite.client.plugins.mkhunter.MKHunterData;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutUntil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PickUpBoneTask extends Task
{

    @Override
    public boolean validate()
    {
        // If we don't have a trap set up:
        if (plugin.canSetupTrap())
        {

            // Has banana to set trap ? Don't loot bone yet
            if (inventory.containsItem(MKHunterData.BANANA))
            {
                return false;
            }
            else
            {
                // No banana so can't set trap ? Can loot bone if we have space
                if (inventory.isFull())
                {
                    return false;
                }
                else
                {
                    return object.getGroundItem(MKHunterData.BONES) != null;
                }
            }
        }
        else
        {
            // If we have a trap set up, we can loot a bone if we have > 1 space (need 1 for damaged tail)
            if (inventory.getEmptySlots() > 1)
            {
                return object.getGroundItem(MKHunterData.BONES) != null;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    private TileItem getNearestTileItem(List<TileItem> tileItems)
    {
        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return tileItems.get(0);
        }

        int currentDistance;
        TileItem closestTileItem = tileItems.get(0);
        int closestDistance = closestTileItem.getTile().getWorldLocation().distanceTo(local.getWorldLocation());
        for (TileItem tileItem : tileItems)
        {
            currentDistance = tileItem.getTile().getWorldLocation().distanceTo(local.getWorldLocation());
            if (currentDistance < closestDistance)
            {
                closestTileItem = tileItem;
                closestDistance = currentDistance;
            }
        }
        return closestTileItem;
    }

    private List<TileItem> getGroundItemsWithin(WorldPoint from, int dist)
    {
        List<TileItem> items = new ArrayList<>();

        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return items;
        }

        if (dist < 0 || from == null)
        {
            return items;
        }

        List<Tile> tiles = new TileQuery()
                .isWithinDistance(from, dist)
                .result(client)
                .list;

        for (Tile t : tiles)
        {
            if (t != null)
            {
                if (t.getGroundItems() != null)
                {
                    items.addAll(t.getGroundItems());
                }
            }
        }

        return items;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        log.info("[PickUpBoneTask] OnGameTick!");
        List<TileItem> items = getGroundItemsWithin(MKHunterData.STAND_TILE, 15);

        if (items.size() == 0)
        {
            return;
        }

        List<TileItem> bonesItems = items.stream().filter(tileItem -> {
            if (tileItem != null)
            {
                return tileItem.getId() == MKHunterData.BONES;
            }
            return false;
        }).collect(Collectors.toList());

        TileItem bones = getNearestTileItem(bonesItems);
        if (bones != null)
        {
            status = "Loot bones";
            int before = inventory.getItemCount(MKHunterData.BONES, false);
            MenuEntry entry = new MenuEntry("Take", "<col=ff9040>Bones", bones.getId(), MenuOpcode.GROUND_ITEM_THIRD_OPTION.getId(), bones.getTile().getSceneLocation().getX(), bones.getTile().getSceneLocation().getY(), false);
            utils.doActionMsTime(entry, bones.getTile().getItemLayer().getCanvasTilePoly().getBounds(), sleepDelay());
            MKHunterPlugin.conditionTimeout = new TimeoutUntil(() -> inventory.getItemCount(MKHunterData.BONES, false) > before, () -> playerUtils.isMoving(MKHunterPlugin.beforeLoc),  5);
        }



    }

}

