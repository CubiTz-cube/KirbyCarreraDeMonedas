package src.utils;

public class CollisionFilters {
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_NO_COLISION_PLAYER = 0x0002;

    public static final short MASK_NO_COLISION_PLAYER = CATEGORY_NO_COLISION_PLAYER;
    public static final short MASK_PLAYER = CATEGORY_PLAYER;
}
