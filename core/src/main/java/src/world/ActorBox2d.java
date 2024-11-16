package src.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.screens.GameScreen;

public abstract class ActorBox2d extends Actor {
    protected final World world;
    protected Body body;
    protected Fixture fixture;

    /**
     * Contructor de la clase ActorBox2d
     * @param world el mundo donde se creara la figura
     * @param shape dimension y popsicion que tendrá
     */
    public ActorBox2d(World world, Rectangle shape){
        this.world = world;
    }

    public Body getBody() {
        return body;
    }

    public void beginContactWith(ActorBox2d actor, GameScreen game){}

    public abstract void detach();
}
