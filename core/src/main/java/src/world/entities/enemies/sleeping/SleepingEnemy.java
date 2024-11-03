package src.world.entities.enemies.sleeping;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.stateMachine.StateMachine;
import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.sleeping.states.SleepingState;
import src.world.entities.enemies.sleeping.states.WalkingState;

import static src.utils.Constants.PIXELS_IN_METER;

public class SleepingEnemy extends Enemy
{
    private Float timeAct = 0f;
    private final float wakeUpTime = 5f;
    private float sleepTimer = 0f;
    private final Sprite sprite;
    private final StateMachine stateMachine;
    private final SleepingState sleepingState;
    private final WalkingState walkingState;

    public SleepingEnemy(World world, Texture texture, Rectangle shape, Integer id, Float crono)
    {
        super(world, id, crono);
        this.sprite = new Sprite(texture);
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("enemy");
        box.dispose();
        body.setFixedRotation(true);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);

        stateMachine = new StateMachine();
        sleepingState = new SleepingState(stateMachine, this);
        walkingState = new WalkingState(stateMachine, this);
        stateMachine.setState(sleepingState);
    }

    public StateMachine getStateMachine()
    {
        return stateMachine;
    }

    public SleepingState getSleepingState()
    {
        return sleepingState;
    }

    public WalkingState getWalkingState()
    {
        return walkingState;
    }

    @Override
    public void act(float delta)
    {
        stateMachine.update(delta);
    }

    public void walk(float delta)
    {
        //Mueve al enemigo y luego vuelve a caminar
        body.setLinearVelocity(3, body.getLinearVelocity().y);
        if (shouldSleep())
        {
            stateMachine.setState(sleepingState);
        }
    }

    public boolean shouldWakeUp()
    {
        //Despierta tras unos segundos
        sleepTimer += Gdx.graphics.getDeltaTime();
        return sleepTimer >= wakeUpTime;
    }

    public boolean shouldSleep()
    {
        //Duerme tras unos segundos
        sleepTimer += Gdx.graphics.getDeltaTime();
        return sleepTimer >= wakeUpTime;
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
