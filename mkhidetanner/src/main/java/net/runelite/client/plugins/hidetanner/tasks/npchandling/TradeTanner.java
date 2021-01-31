package net.runelite.client.plugins.hidetanner.tasks.npchandling;


import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.hidetanner.LeafTask;
import net.runelite.client.plugins.hidetanner.MKHideTannerPlugin;

public class TradeTanner extends LeafTask {

    public TradeTanner() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();

        NPC tannerNPC = npcUtils.findNearestNpc("Ellis");

        if (tannerNPC != null)
        {
            // MenuEntry entry = new MenuEntry("Trade", "<col=fff00>Ellis", tannerNPC.getIndex(), MenuOpcode.NPC_THIRD_OPTION.getId(), 0, 0, false);
            status = "Trade tanner";
            utils.doNpcActionMsTime(tannerNPC, MenuOpcode.NPC_THIRD_OPTION.getId(), sleepDelay());
            MKHideTannerPlugin.timeout = calc.getRandomIntBetweenRange(1, 2);
        }
    }
}
