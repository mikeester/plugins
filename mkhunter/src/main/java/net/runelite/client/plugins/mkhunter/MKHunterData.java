package net.runelite.client.plugins.mkhunter;

import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.coords.WorldPoint;

public class MKHunterData
{

    public static final int GORILLA = NpcID.STUNTED_DEMONIC_GORILLA;

    public static final int BONES = ItemID.BONES, BANANA = ItemID.BANANA, TAB = ItemID.BONES_TO_BANANAS, DAMAGED_TAIL = ItemID.DAMAGED_MONKEY_TAIL,
            MONKEY_TAIL = ItemID.MONKEY_TAIL, GREEGREE = ItemID.KRUK_MONKEY_GREEGREE;

    public static final int EMPTY_BOULDER = ObjectID.LARGE_BOULDER,
            SETUP_BOULDER = ObjectID.MONKEY_TRAP, SETUP_ANIM_BOULDER = ObjectID.LARGE_BOULDER_28825,
            CAUGHT_EAST = ObjectID.LARGE_BOULDER_28830, CAUGHT_SOUTH = ObjectID.LARGE_BOULDER_28831;

    public static final WorldPoint BOULDER_TILE = new WorldPoint(2911, 9128, 0), STAND_TILE = new WorldPoint(2912, 9127, 0);


    /* Monkey traps
        28827 = Set up trap, anim 7256
        28828 = catching a monki east, anim 7252, 7253
        28829 = catching a monki south, anim 7252, 7253
        28832 = east tail being collected, anim 7254, 7258
        28833 = south tail being collected, anim 7254, 7258
        28834 = ?
     */

    /* Large boulders
        28824 = idle
        28825 = being set up, anim 7254, 7255
        28826 = dismantling, anim 7256, 7257
        28830 = Caught with a monkey tail east
        28831 = caught with a monkey tail south
     */


}
