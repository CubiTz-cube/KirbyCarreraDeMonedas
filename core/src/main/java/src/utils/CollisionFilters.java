package src.utils;

public class CollisionFilters {
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_OTHER_PLAYER = 0x0002;

    public static final short MASK_PLAYER = CATEGORY_OTHER_PLAYER;
    public static final short MASK_OTHER_PLAYER = CATEGORY_PLAYER;
}
