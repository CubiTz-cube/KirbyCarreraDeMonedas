package src.world.floors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static src.utils.Constants.PIXELS_IN_METER;

public class Floor extends Actor {
    private final Texture texture;
    private final World world;
    private final Body body;
    private final Fixture fixture;

    public Floor(World world, Texture texture, Rectangle shape){
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("floor");
        box.dispose();

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setPosition(shape.x * PIXELS_IN_METER, shape.y * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {

    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
