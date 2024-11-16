package src.world.entities.enemies.sleeping;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.sleeping.states.*;
import src.world.player.powers.PowerUp;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class SleepingEnemy extends Enemy
{

    public SleepingEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id)
    {
        super(world, shape, assetManager,id);
        type = Type.SLEEPY;
        powerUp = PowerUp.Type.SLEEP;
        sprite = new Sprite(assetManager.get("yozhi.jpg", Texture.class));
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);

        idleState = new IdleStateSleepy(this);
        walkState = new WalkStateSleepy(this);
        setState(StateType.IDLE);
    }

    @Override
    public void act(float delta)
    {
        stateMachine.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
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
    public void detach()
    {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
