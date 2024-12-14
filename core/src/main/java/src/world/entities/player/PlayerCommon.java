package src.world.entities.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.FrontRayCastCallback;
import src.utils.animation.SheetCutter;
import src.utils.stateMachine.StateMachine;
import src.world.entities.Entity;
import src.world.entities.player.powers.*;
import src.world.entities.player.states.*;

import java.util.ArrayList;
import java.util.Random;

import static src.utils.constants.Constants.PIXELS_IN_METER;

public abstract class PlayerCommon extends Entity {
    public float speed = 12;
    public float maxSpeed = 6;
    public float stunTime = DEFAULT_STUNT_TIME;
    public float brakeForce = DEFAULT_BRAKE_FORCE;

    public static final float DEFAULT_STUNT_TIME = 1f;
    public static final float WALK_SPEED = 10f;
    public static final float WALK_MAX_SPEED = 5f;
    public static final float RUN_SPEED = 14f;
    public static final float RUN_MAX_SPEED = 6.5f;
    public static final float MAX_JUMP_TIME = 0.3f;
    public static final float JUMP_IMPULSE = 9f;
    public static final float JUMP_INAIR = 20f; // Se multiplica por deltaTime
    public static final float FLY_IMPULSE = 6f;
    public static final float DASH_IMPULSE = 15f;
    public static final float ABSORB_FORCE = 12f;
    public static final float DEFAULT_BRAKE_FORCE = 280f;

    public AssetManager assetManager;

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
    protected StateType currentStateType;
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

    public enum SoundType{
        AIRSHOT,
        ABSORB1,
        ABSORB2,
        DASH,
        FIREDAMAGE,
        NORMALDAMAGE,
        HEAVYFALL,
        ITEM,
        JUMP,
        POWER,
        SCORE1,
        SCORE2,
        SLEEP,
        STAR,
        REMOVESELECT,
    }
    private Random random;
    private Sound airShotSound;
    private Sound soundAbsorb1;
    private Sound soundAbsorb2;
    private Sound soundDash;
    private Sound soundFireDamage;
    private Sound soundNormalDamage;
    private Sound soundHeavyFall;
    private Sound soundItem;
    private Sound soundJump;
    private Sound soundPower;
    private Sound soundScore1;
    private Sound soundScore2;
    private Sound soundSleep;
    private Sound soundStar;
    private Sound soundRemoveSelect;

    public PlayerCommon(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager, id, null);
        this.assetManager = assetManager;
        stateMachine = new StateMachine();

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        CircleShape box = new CircleShape();
        box.setRadius(shape.width/4);
        fixture = body.createFixture(box, 1.9f);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/4);

        initAnimations(assetManager);
        initPowers();
        initSound();

        setAnimation(AnimationType.IDLE);

        secondSprite = new Sprite();
        secondSprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

        random = new Random();
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

    private void initSound(){
        airShotSound = assetManager.get("sound/kirby/kirbyAirShot.wav");
        soundAbsorb1 = assetManager.get("sound/kirby/kirbyAbsorb1.wav");
        soundAbsorb2 = assetManager.get("sound/kirby/kirbyAbsorb2.wav");
        soundDash = assetManager.get("sound/kirby/kirbyDash.wav");
        soundFireDamage = assetManager.get("sound/kirby/kirbyFireDamage.wav");
        soundNormalDamage = assetManager.get("sound/kirby/kirbyNormalDamage.wav");
        soundHeavyFall = assetManager.get("sound/kirby/kirbyHeavyFall.wav");
        soundItem = assetManager.get("sound/kirby/kirbyItem.wav");
        soundJump = assetManager.get("sound/kirby/kirbyJump.wav");
        soundPower = assetManager.get("sound/kirby/kirbyPower.wav");
        soundScore1 = assetManager.get("sound/kirby/kirbyScore1.wav");
        soundScore2 = assetManager.get("sound/kirby/kirbyScore2.wav");
        soundSleep = assetManager.get("sound/kirby/kirbySleep.wav");
        soundStar = assetManager.get("sound/kirby/kirbyStar.wav");
        soundRemoveSelect = assetManager.get("sound/kirby/kirbyRemovePower.wav");
    }

    public void setAnimation(AnimationType animationType){
        currentAnimationType = animationType;

        if (currentPowerUp != null) {
            setSecondCurrentAnimation(currentPowerUp.getSecondAnimation(animationType));
            Animation<TextureRegion> anima = currentPowerUp.getAnimation(animationType);
            setCurrentAnimation(anima);
            if (anima != null) return;
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

    public void setSecondCurrentAnimation(Animation<TextureRegion> secondCurrentAnimation) {
        System.out.println("Setting second animation " + secondCurrentAnimation);
        System.out.println(currentpowerUptype);
        this.secondCurrentAnimation = secondCurrentAnimation;
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
        if (currentPowerUp != null) currentPowerUp.end();
        if (type == null) {
            currentPowerUp = null;
            currentAnimationType = null;
            return;
        }
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

    public PowerUp.Type getCurrentpowerUptype() {
        return currentpowerUptype;
    }

    public void playSound(SoundType type){
        switch (type){
            case AIRSHOT -> airShotSound.play(1f, 0.9f,0);
            case ABSORB1 -> soundAbsorb1.play(1f, 1f,0);
            case ABSORB2 -> soundAbsorb2.play(1f, 1f,0);
            case DASH -> soundDash.play(1f, 1f,0);
            case FIREDAMAGE -> soundFireDamage.play(1f, 1f,0);
            case NORMALDAMAGE -> soundNormalDamage.play(1f, 1f,0);
            case HEAVYFALL -> soundHeavyFall.play(1f, 1f,0);
            case ITEM -> soundItem.play(1f, 1f,0);
            case JUMP -> soundJump.play(1f, 1f,0);
            case POWER -> soundPower.play(1f, 1f,0);
            case SCORE1 -> soundScore1.play(1f, 1f,0);
            case SCORE2 -> soundScore2.play(1f, random.nextFloat(0.5f,1),0);
            case SLEEP -> soundSleep.play(1f, 1f,0);
            case STAR -> soundStar.play(1f, 1f,0);
            case REMOVESELECT -> soundRemoveSelect.play(1f, 1f,0);
        }
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
        if (currentPowerUp != null) currentPowerUp.update();
    }

    public ArrayList<Fixture> detectFrontFixtures(float distance) {
        ArrayList<Fixture> hitFixtures = new ArrayList<>();
        Vector2 startPoint = body.getPosition();

        Vector2 endPoint = new Vector2(startPoint.x + distance, startPoint.y);
        FrontRayCastCallback callback = new FrontRayCastCallback();
        world.rayCast(callback, startPoint, endPoint);
        if (callback.getHitFixture() != null) {
            hitFixtures.add(callback.getHitFixture());
        }

        endPoint.set(startPoint.x + distance * MathUtils.cosDeg(35), startPoint.y + distance * MathUtils.sinDeg(35));
        callback = new FrontRayCastCallback();
        world.rayCast(callback, startPoint, endPoint);
        if (callback.getHitFixture() != null) {
            hitFixtures.add(callback.getHitFixture());
        }

        endPoint.set(startPoint.x + distance * MathUtils.cosDeg(-35), startPoint.y + distance * MathUtils.sinDeg(-35));
        callback = new FrontRayCastCallback();
        world.rayCast(callback, startPoint, endPoint);
        if (callback.getHitFixture() != null) {
            hitFixtures.add(callback.getHitFixture());
        }

        return hitFixtures;
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
