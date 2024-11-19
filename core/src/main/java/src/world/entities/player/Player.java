package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.main.Main;
import src.screens.GameScreen;
import src.utils.CollisionFilters;
import src.utils.FrontRayCastCallback;
import src.utils.stateMachine.*;
import src.utils.variables.PlayerControl;
import src.world.ActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.mirror.Mirror;
import src.world.entities.player.powers.*;
import src.world.entities.player.states.*;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class Player extends PlayerAnimations
{
    public float speed = 12;
    public float maxSpeed = 6;
    public float stunTime = 2;

    public static final float WALK_SPEED = 10f;
    public static final float WALK_MAX_SPEED = 5f;
    public static final float RUN_SPEED = 14f;
    public static final float RUN_MAX_SPEED = 6.5f;
    public static final float MAX_JUMP_TIME = 0.3f;
    public static final float JUMP_IMPULSE = 8f;
    public static final float JUMP_INAIR = 0.35f;
    public static final float FLY_IMPULSE = 6f;
    public static final float DASH_IMPULSE = 15f;

    public enum StateType {
        IDLE,
        WALK,
        JUMP,
        FALL,
        DOWN,
        RUN,
        DASH,
        FLY,
        ABSORB,
        STUN,
        CONSUME,
    }
    private StateType currentStateType;
    protected final StateMachine stateMachine;
    private final IdleState idleState;
    private final JumpState jumpState;
    private final WalkState walkState;
    private final FallState fallState;
    private final FlyState flyState;
    private final DownState downState;
    private final AbsorbState absorbState;
    private final DashState dashState;
    private final RunState runState;
    private final StunState stunState;
    private final ConsumeState consumeState;

    private Boolean changeAnimation;

    public Enemy enemyAbsorded;
    private PowerUp powerUp;

    private final PowerSleep powerSleep;
    private final PowerSword powerSword;

    public Player(World world, Rectangle shape, AssetManager assetManager)
    {
        super(world, shape, assetManager, -1);
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_PLAYER;
        filter.maskBits = ~CollisionFilters.MASK_OTHERPLAYER;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setSpritePosModification(0f, getHeight()/4);

        stateMachine = new StateMachine();
        idleState = new IdleState(this);
        jumpState = new JumpState(this);
        walkState = new WalkState(this);
        fallState = new FallState(this);
        flyState = new FlyState(this);
        downState = new DownState(this);
        absorbState = new AbsorbState(this);
        dashState = new DashState(this);
        runState = new RunState(this);
        stunState = new StunState(this);
        consumeState = new ConsumeState(this);
        stateMachine.setState(idleState);

        changeAnimation = false;

        powerSleep = new PowerSleep(this);
        powerSword = new PowerSword(this);
    }

    public void consumeEnemy() {
        if (enemyAbsorded == null) return;
        powerUp = switch (enemyAbsorded.getPowerUp()){
            case SLEEP -> powerSleep;
            case SWORD -> powerSword;
            default -> null;
        };
        enemyAbsorded = null;
        setState(Player.StateType.IDLE);
        if (powerUp != null) powerUp.start();
    }

    public void setState(StateType stateType){
        currentStateType = stateType;
        switch (stateType){
            case IDLE -> stateMachine.setState(idleState);
            case WALK -> stateMachine.setState(walkState);
            case JUMP -> stateMachine.setState(jumpState);
            case FALL -> stateMachine.setState(fallState);
            case DOWN -> stateMachine.setState(downState);
            case RUN -> stateMachine.setState(runState);
            case DASH -> stateMachine.setState(dashState);
            case FLY -> stateMachine.setState(flyState);
            case ABSORB -> stateMachine.setState(absorbState);
            case STUN -> stateMachine.setState(stunState);
            case CONSUME -> stateMachine.setState(consumeState);
        }
    }

    public StateType getCurrentStateType() {
        return currentStateType;
    }

    @Override
    public void setAnimation(AnimationType animationType) {
        super.setAnimation(animationType);
        changeAnimation = true;
    }

    public Boolean checkChangeAnimation() {
        if (changeAnimation) {
            changeAnimation = false;
            return true;
        }
        return false;
    }

    @Override
    public void setFlipX(Boolean flipX) {
        super.setFlipX(flipX);
        changeAnimation = true;
    }

    @Override
    public void act(float delta) {
        stateMachine.update(delta);
        Vector2 velocity = body.getLinearVelocity();

        if (currentStateType == StateType.DASH || currentStateType == StateType.STUN) return;
        if (!Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            float brakeForce = 10f;
            body.applyForce(-velocity.x * brakeForce, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Enemy enemy) {
            if (currentStateType == StateType.DASH){
                game.removeEntity(enemy.getId());
                return;
            }

            if (currentStateType == StateType.ABSORB){
                enemyAbsorded = enemy;
                game.removeEntity(enemy.getId());
                setState(Player.StateType.IDLE);
                return;
            }

            setState(Player.StateType.STUN);
            Vector2 pushDirection = body.getPosition().cpy().sub(actor.getBody().getPosition()).nor();
            body.applyLinearImpulse(pushDirection.scl(15.0f), body.getWorldCenter(), true);

        } else if (actor instanceof Mirror) {
            game.threadSecureWorld.addModification(() -> {
                game.getPlayer().getBody().setTransform(game.lobbyPlayer.x, game.lobbyPlayer.y, 0);
                game.main.changeScreen(Main.Screens.MINIDUCK);
                game.randomMirror();
            });
        }
    }

    public Fixture detectFrontFixture(float distance) {
        Vector2 startPoint = body.getPosition();
        Vector2 endPoint = new Vector2(startPoint.x + distance, startPoint.y);

        FrontRayCastCallback callback = new FrontRayCastCallback();
        world.rayCast(callback, startPoint, endPoint);

        return callback.getHitFixture();
    }

    public void attractFixture(Fixture fixture) {
        Vector2 playerPosition = body.getPosition();
        Vector2 fixturePosition = fixture.getBody().getPosition();

        Vector2 direction = playerPosition.cpy().sub(fixturePosition).nor();
        float distance = playerPosition.dst(fixturePosition);
        float forceMagnitude = 10.0f;
        Vector2 force = direction.scl(forceMagnitude * distance);
        fixture.getBody().applyForceToCenter(force, true);
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
