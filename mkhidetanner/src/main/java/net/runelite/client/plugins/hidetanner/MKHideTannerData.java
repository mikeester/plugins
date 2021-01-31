package net.runelite.client.plugins.hidetanner;

import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public class MKHideTannerData {

    public static final String COINS = "Coins", COWHIDE = "Cowhide", GREEN_DHIDE = "Green dragonhide", BLUE_DHIDE = "Blue dragonhide", RED_DHIDE = "Red dragonhide", BLACK_DHIDE = "Black dragonhide",
            SOFT_LEATHER = "Leather", HARD_LEATHER = "Hard leather",
            GREEN_LEATHER = "Green dragon leather", BLUE_LEATHER = "Blue dragon leather", RED_LEATHER = "Red dragon leather", BLACK_LEATHER = "Black dragon leather";

    public static final String[] WANTED_ITEMS = { COINS, COWHIDE, GREEN_DHIDE, BLUE_DHIDE, RED_DHIDE, BLACK_DHIDE };

    public static final int[] WANTED_ITEM_IDS = {ItemID.COINS_995, ItemID.COWHIDE, ItemID.GREEN_DRAGONHIDE, ItemID.BLUE_DRAGONHIDE, ItemID.RED_DRAGONHIDE, ItemID.BLACK_DRAGONHIDE };

    public static final String ELLIS = "Ellis";

    public static final int TAN_INTERFACE_CONTAINER = 324,
            SOFT_TAN_ALL_INDEX = 124, HARD_TAN_ALL_INDEX = 125,
            GREEN_TAN_ALL_INDEX = 128, BLUE_TAN_ALL_INDEX = 129, RED_TAN_ALL_INDEX = 130, BLACK_TAN_ALL_INDEX = 131;

    public static final WorldPoint TANNING_TILE = new WorldPoint(3274, 3191, 0), BANKING_TILE = new WorldPoint(3269, 3166, 0);

    public static final WorldPoint DOOR_OPEN_TILE = new WorldPoint(3278, 3191, 0), DOOR_CLOSED_TILE = new WorldPoint(3277, 3191, 0);
    public static final int DOOR_OPEN_ID = 1536, DOOR_CLOSED_ID = 1535;

    public static final WorldArea TAN_ROOM = new WorldArea(new WorldPoint(3270, 3189, 0), new WorldPoint(3277, 3194, 0)),
            BANK_AREA = new WorldArea(new WorldPoint(3269, 3161, 0), new WorldPoint(3272, 3173, 0)),
            BANK_BOOTH_TILES = new WorldArea(new WorldPoint(3269, 3166, 0), new WorldPoint(3270, 3169, 0)),
            OUTSIDE_TAN_AREA = new WorldArea(new WorldPoint(3279, 3190, 0), new WorldPoint(3281, 3192, 0)),
            INSIDE_TAN_AREA = new WorldArea(new WorldPoint(3271, 3191, 0), new WorldPoint(3273, 3192, 0));

}
