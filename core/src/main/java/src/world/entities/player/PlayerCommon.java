package src.world.entities.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.FrontRayCastCallback;
import src.utils.animation.SheetCutter;
import src.utils.stateMachine.StateMachine;
import src.world.entities.Entity;
import src.world.entities.player.powers.*;
import src.world.entities.player.states.*;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public abstract class PlayerCommon extends Entity {
    public float stunTime = 2;

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
        STAR,
    }
    private StateType currentStateType;
    private final StateMachine stateMachine;
    protected IdleState idleState;
    protected JumpState jumpState;
    protected WalkState walkState;
    protected FallState fallState;
    protected FlyState flyState;
    protected DownState downState;
    protected AbsorbState absorbState;
    protected DashState dashState;
    protected RunState runState;
    protected StunState stunState;
    protected ConsumeState consumeState;
    protected StarState starState;

    public enum AnimationType {
        IDLE,
        WALK,
        JUMP,
        FALL,
        FALLSIMPLE,
        DOWN,
        RUN,
        CHANGERUN,
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
        ABSORBFALL,
        ABSORBJUMP,
    }
    private AnimationType currentAnimationType;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> idleAnimation;
    protected Animation<TextureRegion> jumpAnimation;
    protected Animation<TextureRegion> fallAnimation;
    protected Animation<TextureRegion> fallSimpleAnimation;
    protected Animation<TextureRegion> downAnimation;
    protected Animation<TextureRegion> runAnimation;
    protected Animation<TextureRegion> changeRunAnimation;
    protected Animation<TextureRegion> dashAnimation;
    protected Animation<TextureRegion> flyAnimation;
    protected Animation<TextureRegion> inFlyAnimation;
    protected Animation<TextureRegion> upFlyAnimation;
    protected Animation<TextureRegion> flyEndAnimation;
    protected Animation<TextureRegion> absorbAnimation;
    protected Animation<TextureRegion> damageAnimation;
    protected Animation<TextureRegion> sleepAnimation;
    protected Animation<TextureRegion> consumeAnimation;

    protected  Animation<TextureRegion> absorbIdleAnimation;
    protected  Animation<TextureRegion> absorbWalkAnimation;
    protected  Animation<TextureRegion> absorbRunAnimation;
    protected  Animation<TextureRegion> absorbFallAnimation;
    protected  Animation<TextureRegion> absorbJumpAnimation;

    private final Sprite secondSprite;
    private Animation<TextureRegion> secondCurrentAnimation;

    protected PowerUp.Type currentpowerUptype;
    private PowerUp currentPowerUp;

    private PowerSleep powerSleep;
    private PowerSword powerSword;
    private PowerBomb powerBomb;
    private PowerWheel powerWheel;

    public AssetManager assetManager;

    public PlayerCommon(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager, id, null);
        this.assetManager = assetManager;
        stateMachine = new StateMachine();

        initAnimations(assetManager);
        setAnimation(AnimationType.IDLE);

        initPowers();

        secondSprite = new Sprite();
        secondSprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
    }

    private void initPowers(){
        powerSleep = new PowerSleep(this);
        powerSword = new PowerSword(this);
        powerBomb = new PowerBomb(this);
        powerWheel = new PowerWheel(this);
    }

    private void initAnimations(AssetManager assetManager){
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

        changeRunAnimation = new Animation<>(1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyChangeRun.png"), 1));

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
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbySleep.png"), 33));

        absorbIdleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbIdle.png"), 31));
        absorbIdleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbWalkAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbWalk.png"), 16));
        absorbWalkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbRunAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbWalk.png"), 16));
        absorbRunAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbFallAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbFall.png"), 2));
        absorbFallAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbJumpAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbJump.png"), 4));
    }

    public void setSecondCurrentAnimation(Animation<TextureRegion> secondCurrentAnimation) {
        this.secondCurrentAnimation = secondCurrentAnimation;
    }

    public void setAnimation(AnimationType animationType){
        currentAnimationType = animationType;

        if (currentPowerUp != null){
            setSecondCurrentAnimation(currentPowerUp.getSecondAnimation(animationType));
        }

        switch (animationType){
            case IDLE -> setCurrentAnimation(idleAnimation);
            case WALK -> setCurrentAnimation(walkAnimation);
            case JUMP -> setCurrentAnimation(jumpAnimation);
            case FALL -> setCurrentAnimation(fallAnimation);
            case FALLSIMPLE -> setCurrentAnimation(fallSimpleAnimation);
            case DOWN -> setCurrentAnimation(downAnimation);
            case RUN -> setCurrentAnimation(runAnimation);
            case CHANGERUN -> setCurrentAnimation(changeRunAnimation);
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
            case ABSORBFALL -> setCurrentAnimation(absorbFallAnimation);
            case ABSORBJUMP -> setCurrentAnimation(absorbJumpAnimation);
        }
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }

    public void setCurrentState(Player.StateType stateType){
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
            case STAR -> stateMachine.setState(starState);
        }
    }

    public StateType getCurrentStateType() {
        return currentStateType;
    }

    public void setCurrentPowerUp(PowerUp.Type type){
        currentpowerUptype = type;
        if (type == null) return;
        currentPowerUp = switch (type){
            case BOMB -> powerBomb;
            case SLEEP -> powerSleep;
            case SWORD -> powerSword;
            case WHEEL -> powerWheel;
        };
        currentPowerUp.start();
    }

    public PowerUp getCurrentPowerUp() {
        return currentPowerUp;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (secondCurrentAnimation != null) secondSprite.setRegion(secondCurrentAnimation.getKeyFrame(getAnimateTime(), false));
        if (secondSprite.getTexture() == null) return;
        secondSprite.setFlip(isFlipX(), false);
        secondSprite.setPosition(getX(), getY());
        secondSprite.setOriginCenter();
        secondSprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        stateMachine.update(delta);
    }

    public Fixture detectFrontFixture(float distance) {
        Vector2 startPoint = body.getPosition();
        Vector2 endPoint = new Vector2(startPoint.x + distance, startPoint.y);

        FrontRayCastCallback callback = new FrontRayCastCallback();
        world.rayCast(callback, startPoint, endPoint);

        return callback.getHitFixture();
    }

    public void attractFixture(Fixture fixture, Float forceMagnitude) {
        Vector2 playerPosition = body.getPosition();
        Vector2 fixturePosition = fixture.getBody().getPosition();

        Vector2 direction = playerPosition.cpy().sub(fixturePosition).nor();
        float distance = playerPosition.dst(fixturePosition);
        Vector2 force = direction.scl(forceMagnitude * distance);
        fixture.getBody().applyForceToCenter(force, true);
    }
}
