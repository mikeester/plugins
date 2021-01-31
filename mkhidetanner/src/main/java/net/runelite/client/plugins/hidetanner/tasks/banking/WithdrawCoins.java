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
public class WithdrawCoins extends LeafTask {

    public WithdrawCoins() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        if (bank.contains(MKHideTannerData.COINS)) {
            Widget coins = bank.getBankItemWidget(ItemID.COINS_995);
            if (coins != null)
            {
                int quantity = coins.getItemQuantity();
                quantity = Math.min(500_000, quantity - 1); // Withdraw all-but-1 gp and cap at 500k
                status = "Withdraw coins";
                bank.withdrawItemAmount(ItemID.COINS, quantity);
                MKHideTannerPlugin.timeout = calc.getRandomIntBetweenRange(3, 4);
            }
        } else {
            log.info("[MK] Stopping hide tanner, no more coins in bank");
            MKHideTannerPlugin.shouldStop = true;
        }
    }
}
