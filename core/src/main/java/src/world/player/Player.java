package src.world.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.utils.FrontRayCastCallback;
import src.utils.animation.SheetCutter;
import src.utils.stateMachine.*;
import src.world.SpriteActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.player.powers.PowerUp;
import src.world.player.states.*;

import static src.utils.Constants.PIXELS_IN_METER;

public class Player extends SpriteActorBox2d
{
    public float speed = 10;
    public float maxSpeed = 4;
    public static final float MAX_JUMP_TIME = 0.3f;
    public static final float JUMP_IMPULSE = 6f;
    public static final float JUMP_INAIR = 0.3f;
    public static final float DASH_IMPULSE = 10f;

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
        STUNT
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
    private final StuntState stuntState;

    public enum AnimationType {
        IDLE,
        WALK,
        JUMP,
        FALL,
        DOWN,
        RUN,
        DASH,
        FLY,
        INFLY,
        UPFLY,
        ABSORB,
        DAMAGE
    }
    private Boolean changeAnimation;
    private AnimationType currentAnimationType;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> dashAnimation;
    private final Animation<TextureRegion> flyAnimation;
    private final Animation<TextureRegion> inFlyAnimation;
    private final Animation<TextureRegion> upFlyAnimation;
    private final Animation<TextureRegion> absorbAnimation;
    private final Animation<TextureRegion> damageAnimation;

    private PowerUp powerUp;

    public Player(World world, AssetManager assetManager, Rectangle shape)
    {
        super(world);
        sprite = new Sprite();
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData("player");
        box.dispose();
        body.setFixedRotation(true);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_PLAYER;
        filter.maskBits = ~CollisionFilters.MASK_OTHERPLAYER;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setSpritePosModification(0f, getHeight()/4);

        stateMachine = new StateMachine();
        idleState = new IdleState(stateMachine, this);
        jumpState = new JumpState(stateMachine, this);
        walkState = new WalkState(stateMachine, this);
        fallState = new FallState(stateMachine, this);
        flyState = new FlyState(stateMachine, this);
        downState = new DownState(stateMachine, this);
        absorbState = new AbsorbState(stateMachine, this);
        dashState = new DashState(stateMachine, this);
        runState = new RunState(stateMachine, this);
        stuntState = new StuntState(stateMachine, this);
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
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFall.png"), 22));

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

        absorbAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyAbsorb.png"), 7));

        damageAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDamage.png"), 9));

        setAnimation(AnimationType.IDLE);
        changeAnimation = false;
    }

    public void setPowerUp(Enemy enemy) {

        /*this.powerUp = switch (enemy.getPowerUp()){
            case NULL -> null;
            case SLEEP ->
        }*/
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
            case STUNT -> stateMachine.setState(stuntState);
        }
    }

    public Boolean checkChangeAnimation() {
        if (changeAnimation) {
            changeAnimation = false;
            return true;
        }
        return false;
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
            case DOWN -> setCurrentAnimation(downAnimation);
            case RUN -> setCurrentAnimation(runAnimation);
            case DASH -> setCurrentAnimation(dashAnimation);
            case FLY -> setCurrentAnimation(flyAnimation);
            case INFLY -> setCurrentAnimation(inFlyAnimation);
            case UPFLY -> setCurrentAnimation(upFlyAnimation);
            case ABSORB -> setCurrentAnimation(absorbAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
        }
    }

    @Override
    public void setFlipX(Boolean flipX) {
        super.setFlipX(flipX);
        changeAnimation = true;
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }

    @Override
    public void act(float delta) {
        stateMachine.update(delta);
        Vector2 velocity = body.getLinearVelocity();

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            float brakeForce = 10f;
            body.applyForce(-velocity.x * brakeForce, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public Boolean isOnGround(){
        return body.getLinearVelocity().y == 0;
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
