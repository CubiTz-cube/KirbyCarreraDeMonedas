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
import com.badlogic.gdx.physics.box2d.*;
import src.utils.FrontRayCastCallback;
import src.utils.animation.SheetCutter;
import src.utils.managers.SoundManager;
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
    public int dashDamage = DEFAULT_DASH_DAMAGE;

    public static final int DEFAULT_DASH_DAMAGE = 1;
    public static final float DEFAULT_STUNT_TIME = 1f;
    public static final float WALK_SPEED = 14f;
    public static final float WALK_MAX_SPEED = 5f;
    public static final float RUN_SPEED = 18f;
    public static final float RUN_MAX_SPEED = 6.5f;
    public static final float MAX_JUMP_TIME = 0.3f;
    public static final float JUMP_IMPULSE = 8f;
    public static final float JUMP_INAIR = 25f; // Se multiplica por deltaTime
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
    private final Random random;
    private Sound airShotSound;
    private Sound absorb1Sound;
    private Sound absorb2Sound;
    private Sound dashSound;
    private Sound fireDamageSound;
    private Sound normalDamageSound;
    private Sound heavyFallSound;
    private Sound itemSound;
    private Sound jumpSound;
    private Sound powerSound;
    private Sound score1Sound;
    private Sound score2Sound;
    private Sound sleepSound;
    private Sound starSound;
    private Sound removeSelectSound;

    protected float bodyWidth, bodyHeight;

    public PlayerCommon(World world, Float x, Float y, AssetManager assetManager, Integer id) {
        super(world, new Rectangle(x,y,2.25f,2.25f), assetManager, id, null);
        bodyHeight = bodyWidth = 2.25f;
        this.assetManager = assetManager;
        stateMachine = new StateMachine();

        BodyDef def = new BodyDef();
        def.position.set(x + (bodyWidth-1) / 2, y + (bodyHeight-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        CircleShape box = new CircleShape();
        box.setRadius(bodyWidth/6);
        fixture = body.createFixture(box, 1.9f);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/3);

        initAnimations(assetManager);
        initPowers();
        initSound();

        setAnimation(AnimationType.IDLE);

        secondSprite = new Sprite();
        secondSprite.setSize(bodyWidth * PIXELS_IN_METER, bodyHeight * PIXELS_IN_METER);

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
        absorb1Sound = assetManager.get("sound/kirby/kirbyAbsorb1.wav");
        absorb2Sound = assetManager.get("sound/kirby/kirbyAbsorb2.wav");
        dashSound = assetManager.get("sound/kirby/kirbyDash.wav");
        fireDamageSound = assetManager.get("sound/kirby/kirbyFireDamage.wav");
        normalDamageSound = assetManager.get("sound/kirby/kirbyNormalDamage.wav");
        heavyFallSound = assetManager.get("sound/kirby/kirbyHeavyFall.wav");
        itemSound = assetManager.get("sound/kirby/kirbyItem.wav");
        jumpSound = assetManager.get("sound/kirby/kirbyJump.wav");
        powerSound = assetManager.get("sound/kirby/kirbyPower.wav");
        score1Sound = assetManager.get("sound/kirby/kirbyScore1.wav");
        score2Sound = assetManager.get("sound/kirby/kirbyScore2.wav");
        sleepSound = assetManager.get("sound/kirby/kirbySleep.wav");
        starSound = assetManager.get("sound/kirby/kirbyStar.wav");
        removeSelectSound = assetManager.get("sound/kirby/kirbyRemovePower.wav");
    }

    public void setAnimation(AnimationType animationType){
        if (animationType == null) return;
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
        if (type == PowerUp.Type.NONE || type == null) {
            currentPowerUp = null;
            currentAnimationType = null;
            return;
        }
        currentPowerUp = switch (type){
            case BOMB -> powerBomb;
            case SLEEP -> powerSleep;
            case SWORD -> powerSword;
            case WHEEL -> powerWheel;
            default -> null;
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
            case AIRSHOT -> SoundManager.playSound(airShotSound, 0.9f);
            case ABSORB1 -> SoundManager.playSound(absorb1Sound, 1f);
            case ABSORB2 -> SoundManager.playSound(absorb2Sound, 1f);
            case DASH -> SoundManager.playSound(dashSound, 1f);
            case FIREDAMAGE -> SoundManager.playSound(fireDamageSound, 1f);
            case NORMALDAMAGE -> SoundManager.playSound(normalDamageSound, 1f);
            case HEAVYFALL -> SoundManager.playSound(heavyFallSound, 1f);
            case ITEM -> SoundManager.playSound(itemSound, 1f);
            case JUMP -> SoundManager.playSound(jumpSound, 1f);
            case POWER -> SoundManager.playSound(powerSound, 1f);
            case SCORE1 -> SoundManager.playSound(score1Sound, 1f);
            case SCORE2 -> SoundManager.playSound(score2Sound, 1f);
            case SLEEP -> SoundManager.playSound(sleepSound, 1f);
            case STAR -> SoundManager.playSound(starSound, 1f);
            case REMOVESELECT -> SoundManager.playSound(removeSelectSound, 1f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (secondCurrentAnimation != null) secondSprite.setRegion(secondCurrentAnimation.getKeyFrame(getAnimateTime(), false));
        else secondSprite.setTexture(null);
        if (secondSprite.getTexture() == null) return;
        secondSprite.setFlip(isFlipX(), false);
        secondSprite.setPosition(getX(), getY());
        secondSprite.setOriginCenter();
        secondSprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        stateMachine.update(delta);
        if (currentPowerUp != null) currentPowerUp.update(delta);
    }
}
