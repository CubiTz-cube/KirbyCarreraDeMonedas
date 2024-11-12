package src.world;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class ActorBox2d extends Actor {
    protected final World world;
    protected Body body;
    protected Fixture fixture;

    /**
     * Contructor de la clase ActorBox2d
     * @param world el mundo donde se creara la figura
     */
    public ActorBox2d(World world){
        this.world = world;
    }

    public Body getBody() {
        return body;
    }

    public abstract void detach();
}
