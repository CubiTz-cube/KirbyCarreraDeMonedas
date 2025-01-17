package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.world.ActorBox2dSprite;

public abstract class Entity extends ActorBox2dSprite {
    public enum Type{
        BASIC,
        SLEEPY,
        SWORD,
        WHEEL,
        BOMBER,
        TURRET,
        DRAGON,
        FLYBUG,
        MIRROR,
        CLOUD,
        STAR,
        BOMB,
        BOMBEXPLOSION,
        ICE,
        SWORDRUNPROPLAYER,
        SWORDPROPLAYER,
        SWORDPROENEMY,
        TURRETPROENEMY,
        COIN,
        POWERSWORD,
        POWERWHEEL,
        POWERBOMB,
        PLATFORMXR,
        PLATFORMXL,
        PLATFORMYU,
        PLATFORMYD,
        PLATFORMRU,
        PLATOFRMRD,
        PLATFORMLU,
        PLATFORMLD,
        FALLBLOCK,
        BREAKBLOCK,
        SWORDBREAKBLOCK,
        WATERBLOCKR,
        WATERBLOCKL,
        WATERBLOCKD,
        BOMBBREAKBLOCK,
    }
    protected Type type;
    private final Integer id;

    public Entity(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type){
        super(world, shape, assetManager);
        this.id = id;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }
}
