package src.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.entities.player.states.*;
import src.utils.stateMachine.*;

import static src.utils.Constants.PIXELS_IN_METER;

public class Player extends Actor {
    private final Texture texture;
    private final World world;
    private final Body body;
    private final Fixture fixture;

    private final StateMachine stateMachine;
    private final IdleState idleState;
    private final JumpState jumpState;

    public Player(World world, Texture texture, Vector2 position){
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(box, 3);
        fixture.setUserData("player");
        box.dispose();

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
        stateMachine.update();
    }

    public void controller(){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            body.applyLinearImpulse(1, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            body.applyLinearImpulse(-1, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public void jump(){
        body.applyLinearImpulse(0, 10, body.getWorldCenter().x, body.getWorldCenter().y, true);
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
