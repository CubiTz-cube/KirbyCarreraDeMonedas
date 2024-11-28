package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import src.world.ActorBox2d;
import src.world.SpriteActorBox2d;

public abstract class Entity extends SpriteActorBox2d {
    public enum Type{
        BASIC,
        SLEEPY,
        SWORD,
        //WHEEL,
        //BOMBER,
        //TURRET,
        //DRAGON,
        //FLYBUG,
        MIRROR,
        CLOUD,
        STAR,
        COIN,
        BREAKBLOCK,
        PLATFORMXR,
        PLATFORMXL,
        PLATFORMYU,
        PLATFORMYD,
        PLATFORMRU,
        PLATOFRMRD,
        PLATFORMLU,
        PLATFORMLD,
        FALLBLOCK,
        //WATERIMPULSE,
        //BOMBBREAKBLOCK,
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
