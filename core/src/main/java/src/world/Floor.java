package src.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static src.utils.Constants.PIXELS_IN_METER;

public class Floor extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    public Floor(World world, Texture texture, Vector2 position, Vector2 size){
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(position.x + (size.x-1) / 2, position.y + (size.y-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(size.x / 2, size.y / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("floor");
        box.dispose();

        setSize(PIXELS_IN_METER * size.x, PIXELS_IN_METER * size.y);
        setPosition(position.x * PIXELS_IN_METER, position.y * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {

    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
