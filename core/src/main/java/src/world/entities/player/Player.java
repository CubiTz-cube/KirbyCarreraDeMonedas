package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.world.entities.Entity;
import src.utils.stateMachine.*;
import src.world.entities.player.states.*;

import static src.utils.Constants.PIXELS_IN_METER;

public class Player extends Entity {
    private static final float SPEED = 10;
    private static final float MAX_SPEED = 4;
    public static final float MAX_JUMP_TIME = 0.2f;
    public static final float JUMP_IMPULSE = 5f;
    public static final float JUMP_INAIR = 0.3f;

    private final Sprite sprite;
    private final World world;
    private final Body body;
    private final Fixture fixture;
    private final Fixture sensorFixture;

    protected final StateMachine stateMachine;
    private final IdleState idleState;
    private final JumpState jumpState;
    private final FlyState flyState;
    private final WalkState walkState;
    private final FallState fallState;

    public Player(World world, Texture texture, Rectangle shape){
        this.world = world;
        this.sprite = new Sprite(texture);

        BodyDef def = new BodyDef();
        def.position.set(shape.x, shape.y);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width, shape.height);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("player");
        box.dispose();
        body.setFixedRotation(true);

        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(shape.width/2, 0.2f, new Vector2(0, -0.4f), 0);
        FixtureDef sensorDef = new FixtureDef();
        sensorDef.shape = sensorShape;
        sensorDef.isSensor = true;
        sensorFixture = body.createFixture(sensorDef);
        sensorFixture.setUserData("playerBottomSensor");
        sensorShape.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

        stateMachine = new StateMachine();
        idleState = new IdleState(stateMachine, this);
        jumpState = new JumpState(stateMachine, this);
        flyState = new FlyState(stateMachine, this);
        walkState = new WalkState(stateMachine, this);
        fallState = new FallState(stateMachine, this);
        stateMachine.setState(idleState);
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public IdleState getIdleState() {
        return idleState;
    }

    public JumpState getJumpState() {
        return jumpState;
    }

    public FlyState getFlyState() {
        return flyState;
    }

    public WalkState getWalkState() {
        return walkState;
    }

    public FallState getFallState() {
        return fallState;
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
        controller();
        stateMachine.update(delta);
    }

    private void controller(){
        Vector2 velocity = body.getLinearVelocity();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && velocity.x < MAX_SPEED){
            body.applyForce(SPEED, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && velocity.x > -MAX_SPEED){
            body.applyForce(-SPEED, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            float brakeForce = 10f;
            body.applyForce(-velocity.x * brakeForce, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public void detach(){
        body.destroyFixture(fixture);
        body.destroyFixture(sensorFixture);
        world.destroyBody(body);
    }
}
