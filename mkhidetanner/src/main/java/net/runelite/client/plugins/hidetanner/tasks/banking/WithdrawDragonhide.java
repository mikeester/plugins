package net.runelite.client.plugins.hidetanner.tasks.banking;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerData;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

/**
 * NOTES:
 *
 */
@Slf4j
public class WithdrawDragonhide extends LeafTask {

    public WithdrawDragonhide() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        if (bank.contains(MKHideTannerPlugin.hideID, 1)) {
            int freeSlots = inventory.getEmptySlots();
            Widget dhideWidget = bank.getBankItemWidget(MKHideTannerPlugin.hideID);
            if (dhideWidget != null) {
                int quantity = dhideWidget.getItemQuantity();
                quantity = Math.min(freeSlots, quantity); // Fill inventory, or just withdraw all if less than that in bank
                status = "Withdraw hide";
                bank.withdrawItemAmount(MKHideTannerPlugin.hideID, quantity);
                MKHideTannerPlugin.timeout = calc.getRandomIntBetweenRange(3, 4);
            }
        } else {
            log.info("[MK] Stopping hide tanner, no more dhide in bank");
            MKHideTannerPlugin.shouldStop = true;
        }
    }

}
