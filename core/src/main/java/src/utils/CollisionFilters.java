package src.utils;

public class CollisionFilters {
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_OTHERPLAYER = 0x0002;
    public static final short CATEGORY_ENEMY = 0x0003;
    public static final short CATEGORY_STATIC = 0x0004;
    public static final short CATEGORY_COIN = 0x0005;

    public static final short MASK_OTHERPLAYER = CATEGORY_OTHERPLAYER;
    public static final short MASK_PLAYER = CATEGORY_PLAYER;
    public static final short MASK_ENEMY = CATEGORY_ENEMY;
    public static final short MASK_STATIC = CATEGORY_STATIC;
    public static final short MASK_COIN = CATEGORY_COIN;
}
