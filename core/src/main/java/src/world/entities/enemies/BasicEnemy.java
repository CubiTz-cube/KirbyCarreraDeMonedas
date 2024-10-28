package src.world.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.world.entities.Entity;

import static src.utils.Constants.PIXELS_IN_METER;

public class BasicEnemy extends Entity {
    private final Sprite sprite;
    private final World world;
    private final Body body;
    private final Fixture fixture;

    private Float timeAct = 0f;

    public BasicEnemy(World world, Texture texture, Rectangle shape, Integer id) {
        super(id);
        this.world = world;
        this.sprite = new Sprite(texture);

        BodyDef def = new BodyDef();
        def.position.set(shape.x, shape.y);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width, shape.height);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("sleepingEnemy");
        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);
    }

    public Body getBody() {
        return body;
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(
            body.getPosition().x * PIXELS_IN_METER,
            body.getPosition().y * PIXELS_IN_METER
        );
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
        sprite.setOriginCenter();
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        timeAct += delta;

        if (timeAct < 3) {
            body.setLinearVelocity(-3,body.getLinearVelocity().y);
        }else if(timeAct < 6){
            body.setLinearVelocity(3,body.getLinearVelocity().y);
            //body.applyForce(-5f, 0, body.getPosition().x, body.getPosition().y, true);
        }else if (timeAct > 9){
            timeAct = 0f;
        }
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
