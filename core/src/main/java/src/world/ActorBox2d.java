package src.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.screens.GameScreen;

import static src.utils.constants.Constants.PIXELS_IN_METER;

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
        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setPosition(shape.x * PIXELS_IN_METER, shape.y * PIXELS_IN_METER);
    }

    public Body getBody() {
        return body;
    }

    public void beginContactWith(ActorBox2d actor, GameScreen game){}

    public void detach(){
        body.destroyFixture(fixture);
        for (int i = 0; i< body.getFixtureList().size; i++){
            body.destroyFixture(body.getFixtureList().get(i));
        }
        world.destroyBody(body);

        //System.out.println( this + " liberado del mundo " + fixture + body);
    }
}
