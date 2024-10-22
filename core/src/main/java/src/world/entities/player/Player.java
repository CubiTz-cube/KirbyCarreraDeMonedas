package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.world.ActorBox2d;
import src.utils.stateMachine.*;
import src.world.entities.player.states.*;

import static src.utils.Constants.PIXELS_IN_METER;

public class Player extends ActorBox2d {
    private static final float SPEED = 10;
    private static final float MAX_SPEED = 4;
    public static final float MAX_JUMP_TIME = 0.2f; // Maximum time the jump key can be held
    public static final float JUMP_IMPULSE = 5f;
    public static final float JUMP_INAIR = 0.3f;

    private final Texture texture;
    private final World world;
    private final Body body;
    private final Fixture fixture;

    private final StateMachine stateMachine;
    private final IdleState idleState;
    private final JumpState jumpState;

    public Player(World world, Texture texture, Rectangle shape){
        this.world = world;
        this.texture = texture;

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

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

        stateMachine = new StateMachine();
        idleState = new IdleState(stateMachine, this);
        jumpState = new JumpState(stateMachine, this);
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

    public Body getBody() {
        return body;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(
            body.getPosition().x * PIXELS_IN_METER,
            body.getPosition().y * PIXELS_IN_METER
        );
        float rotation = (float) Math.toDegrees(body.getAngle());
        batch.draw(
            texture,
            getX(), getY(),
            getWidth() / 2, getHeight() / 2,
            getWidth(), getHeight(),
            1, 1,
            rotation,
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false
        );
    }

    @Override
    public void act(float delta) {
        controller();
        stateMachine.update(delta);
    }

    public void controller(){
        Vector2 velocity = body.getLinearVelocity();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && velocity.x < MAX_SPEED){
            body.applyForce(SPEED, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && velocity.x > -MAX_SPEED){
            body.applyForce(-SPEED, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        } else {
            // Apply braking force when no key is pressed
            float brakeForce = 10f; // Adjust this value to increase/decrease braking force
            body.applyForce(-velocity.x * brakeForce, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public void jump(){
        body.applyLinearImpulse(0, 6, body.getWorldCenter().x, body.getWorldCenter().y, true);
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
