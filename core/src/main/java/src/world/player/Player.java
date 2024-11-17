package src.world.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.main.Main;
import src.screens.GameScreen;
import src.utils.CollisionFilters;
import src.utils.FrontRayCastCallback;
import src.utils.animation.SheetCutter;
import src.utils.stateMachine.*;
import src.utils.variables.PlayerControl;
import src.world.ActorBox2d;
import src.world.SpriteActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.mirror.Mirror;
import src.world.player.powers.PowerSleep;
import src.world.player.powers.PowerUp;
import src.world.player.states.*;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class Player extends SpriteActorBox2d
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

    public enum AnimationType {
        IDLE,
        WALK,
        JUMP,
        FALL,
        FALLSIMPLE,
        DOWN,
        RUN,
        DASH,
        FLY,
        FLYIN,
        FLYUP,
        FLYEND,
        ABSORB,
        DAMAGE,
        CONSUME,
        SLEEP,
        ABSORBIDLE,
        ABSORBWALK,
        ABSORBRUN,
    }
    private Boolean changeAnimation;
    private AnimationType currentAnimationType;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;
    private final Animation<TextureRegion> fallSimpleAnimation;
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> dashAnimation;
    private final Animation<TextureRegion> flyAnimation;
    private final Animation<TextureRegion> inFlyAnimation;
    private final Animation<TextureRegion> upFlyAnimation;
    private final Animation<TextureRegion> flyEndAnimation;
    private final Animation<TextureRegion> absorbAnimation;
    private final Animation<TextureRegion> damageAnimation;
    private final Animation<TextureRegion> sleepAnimation;
    private final Animation<TextureRegion> consumeAnimation;

    private final Animation<TextureRegion> absorbIdleAnimation;
    private final Animation<TextureRegion> absorbWalkAnimation;
    private final Animation<TextureRegion> absorbRunAnimation;

    public Enemy enemyAbsorded;
    private PowerUp powerUp;
    private final PowerSleep powerSleep;

    public Player(World world, Rectangle shape, AssetManager assetManager)
    {
        super(world, shape, assetManager);
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

        walkAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyWalk.png"), 10));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        idleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyIdle.png"), 31));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        downAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDown.png"), 31));
        downAnimation.setPlayMode(Animation.PlayMode.LOOP);

        jumpAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyJump.png"), 1));

        fallAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFall.png"), 26));

        fallSimpleAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFallSimple.png"), 20));

        runAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyRun.png"), 8));
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);

        dashAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDash.png"), 2));

        flyAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFly.png"), 5));

        inFlyAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyInFly.png"), 2));
        inFlyAnimation.setPlayMode(Animation.PlayMode.LOOP);

        upFlyAnimation = new Animation<>(0.06f,
        SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyUpFly.png"), 6));

        flyEndAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFlyEnd.png"), 2));

        absorbAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyAbsorb.png"), 16));

        damageAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDamage.png"), 9));

        consumeAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyConsume.png"), 6));

        sleepAnimation = new Animation<>(0.15f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/sleep/sleep.png"), 33));

        absorbIdleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbIdle.png"), 31));
        absorbIdleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbWalkAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbWalk.png"), 16));
        absorbWalkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbRunAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbWalk.png"), 16));
        absorbRunAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setAnimation(AnimationType.IDLE);
        changeAnimation = false;

        powerSleep = new PowerSleep(this);

    }

    public void consumeEnemy() {
        if (enemyAbsorded == null) return;
        powerUp = switch (enemyAbsorded.getPowerUp()){
            case SLEEP -> powerSleep;
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

    public void setAnimation(AnimationType animationType){
        currentAnimationType = animationType;
        changeAnimation = true;
        switch (animationType){
            case IDLE -> setCurrentAnimation(idleAnimation);
            case WALK -> setCurrentAnimation(walkAnimation);
            case JUMP -> setCurrentAnimation(jumpAnimation);
            case FALL -> setCurrentAnimation(fallAnimation);
            case FALLSIMPLE -> setCurrentAnimation(fallSimpleAnimation);
            case DOWN -> setCurrentAnimation(downAnimation);
            case RUN -> setCurrentAnimation(runAnimation);
            case DASH -> setCurrentAnimation(dashAnimation);
            case FLY -> setCurrentAnimation(flyAnimation);
            case FLYIN -> setCurrentAnimation(inFlyAnimation);
            case FLYUP -> setCurrentAnimation(upFlyAnimation);
            case FLYEND -> setCurrentAnimation(flyEndAnimation);
            case ABSORB -> setCurrentAnimation(absorbAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
            case CONSUME -> setCurrentAnimation(consumeAnimation);
            case SLEEP -> setCurrentAnimation(sleepAnimation);
            case ABSORBIDLE -> setCurrentAnimation(absorbIdleAnimation);
            case ABSORBWALK -> setCurrentAnimation(absorbWalkAnimation);
            case ABSORBRUN -> setCurrentAnimation(absorbRunAnimation);
        }
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
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
