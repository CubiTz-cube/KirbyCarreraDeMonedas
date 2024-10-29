package src.world.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import src.world.ActorBox2d;
import src.world.SpriteActorBox2d;

public abstract class Entity extends SpriteActorBox2d {
    private final Integer id;

    public Entity(World world, Integer id){
        super(world);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
