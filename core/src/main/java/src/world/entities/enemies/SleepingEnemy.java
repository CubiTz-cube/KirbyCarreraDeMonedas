package src.world.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.world.entities.Entity;

import static src.utils.Constants.PIXELS_IN_METER;

public class SleepingEnemy extends Entity
{

    public SleepingEnemy(World world, Texture texture, Rectangle shape, Integer id)
    {
        super(world,id);

        this.sprite = new Sprite(texture);
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/2, shape.height/2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("enemy");
        box.dispose();
        body.setFixedRotation(true);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
    }

    @Override
    public void act(float delta)
    {
        //Dormir
    }

    public void detach()
    {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
