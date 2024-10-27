package src.world.entities;

import com.badlogic.gdx.physics.box2d.Body;
import src.world.ActorBox2d;

public abstract class Entity extends ActorBox2d {
    private Integer id;

    protected Body body;

    public Entity(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }
}
