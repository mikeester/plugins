package net.runelite.client.plugins.mkhunter.tasks.falconry;

import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;

public enum Kebbit
{
    SPOTTED(NpcID.SPOTTED_KEBBIT, 43, ItemID.SPOTTED_KEBBIT_FUR, NpcID.GYR_FALCON),
    DARK(NpcID.DARK_KEBBIT, 57, ItemID.DARK_KEBBIT_FUR, NpcID.GYR_FALCON_1344),
    DASHING(NpcID.DASHING_KEBBIT, 69, ItemID.DASHING_KEBBIT_FUR, NpcID.GYR_FALCON_1343);

    @Getter(AccessLevel.PACKAGE)
    public final int npcId;

    @Getter(AccessLevel.PACKAGE)
    public final int hunterLvl;

    @Getter(AccessLevel.PACKAGE)
    public final int furId;

    @Getter(AccessLevel.PACKAGE)
    public final int falconId;

    Kebbit(int npcId, int hunterLvl, int furId, int falconId)
    {
        this.npcId = npcId;
        this.hunterLvl = hunterLvl;
        this.furId = furId;
        this.falconId = falconId;
    }
}
