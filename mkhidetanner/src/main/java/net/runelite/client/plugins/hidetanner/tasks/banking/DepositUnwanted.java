package net.runelite.client.plugins.hidetanner.tasks.banking;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * NOTES:
 *
 */
public class DepositUnwanted extends LeafTask {

    public DepositUnwanted() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        status = "Deposit unwanted items";
        bank.depositAllExcept(Arrays.stream(MKHideTannerData.WANTED_ITEM_IDS).boxed().collect(Collectors.toList()));
    }
}
