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
import src.utils.animation.SheetCutter;
import src.utils.stateMachine.*;
import src.world.SpriteActorBox2d;
import src.world.player.powers.PowerUp;
import src.world.player.states.*;

import java.util.Random;

import static src.utils.Constants.PIXELS_IN_METER;

public class Player extends SpriteActorBox2d
{
    public float speed = 10;
    public float maxSpeed = 4;
    public static final float MAX_JUMP_TIME = 0.3f;
    public static final float JUMP_IMPULSE = 5f;
    public static final float JUMP_INAIR = 0.3f;
    public static final float DASH_IMPULSE = 10f;

    private final Fixture absorbRFixture;
    private final Fixture absorbLFixture;

    protected final StateMachine stateMachine;
    private final IdleState idleState;
    private final JumpState jumpState;
    private final WalkState walkState;
    private final FallState fallState;
    private final FlyState flyState;
    private final DashState dashState;
    private final DownState downState;
    private final RunState runState;
    private final AbsorbState absorbState;

    private final Random random;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> runAnimation;
    /*private final Animation<TextureRegion> dashAnimation;
    private final Animation<TextureRegion> flyAnimation;
    private final Animation<TextureRegion> absorbAnimation;*/

    private PowerUp powerUp;

    public Player(World world, AssetManager assetManager, Rectangle shape)
    {
        super(world);
        setDebug(true);
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

        PolygonShape absorbShapeR = new PolygonShape();
        absorbShapeR.setAsBox(shape.width/4, shape.height/4, new Vector2(shape.width/8, 0), 0);
        FixtureDef absorbRDef = new FixtureDef();
        absorbRDef.shape = absorbShapeR;
        absorbRDef.isSensor = true;
        absorbRFixture = body.createFixture(absorbRDef);
        absorbRFixture.setUserData("playerAbsorbRSensor");
        absorbShapeR.dispose();

        PolygonShape absorbShapeL = new PolygonShape();
        absorbShapeL.setAsBox(shape.width/4, shape.height/4, new Vector2(-shape.width/8, 0), 0);
        FixtureDef absorbLDef = new FixtureDef();
        absorbLDef.shape = absorbShapeL;
        absorbLDef.isSensor = true;
        absorbLFixture = body.createFixture(absorbLDef);
        absorbLFixture.setUserData("playerAbsorbLSensor");
        absorbShapeL.dispose();

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_PLAYER;
        filter.maskBits = ~CollisionFilters.MASK_PLAYER;
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
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyRun.png"), 22));

        /*dashAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDash.png"), 22));

        flyAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFly.png"), 22));

        absorbAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyAbsorb.png"), 22));*/

        currentAnimation = idleAnimation;
    }

    public StateMachine getStateMachine()
    {
        return stateMachine;
    }

    public IdleState getIdleState()
    {
        return idleState;
    }

    public State getRunState()
    {
        return new RunState(stateMachine, this);
    }

    public JumpState getJumpState()
    {
        return jumpState;
    }

    public WalkState getWalkState()
    {
        return walkState;
    }

    public FallState getFallState()
    {
        return fallState;
    }

    public FlyState getFlyState()
    {
        return flyState;
    }

    public Animation<TextureRegion> getIdleAnimation()
    {
    public DownState getDownState() {
        return downState;
    }

    public RunState getRunState() {
        return runState;
    }

    public AbsorbState getAbsorbState() {
        return absorbState;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public Animation<TextureRegion> getWalkAnimation()
    {
        return walkAnimation;
    }

    public boolean isOnGround()
    {
        return Math.abs(body.getLinearVelocity().y) < 0.01f;
    }

    public State getDashState()
    {
        return new DashState(stateMachine, this);
    }

    public State getDownState()
    {
        return new DownState(stateMachine, this);
    }

    public Animation<TextureRegion> getJumpAnimation() {
        return jumpAnimation;
    }

    public Animation<TextureRegion> getFallAnimation() {
        return fallAnimation;
    }

    public Animation<TextureRegion> getDownAnimation() {
        return downAnimation;
    }

    public Animation<TextureRegion> getRunAnimation() {
        return runAnimation;
    }

    /*public Animation<TextureRegion> getFlyAnimation() {
        return flyAnimation;
    }

    public Animation<TextureRegion> getDashAnimation() {
        return dashAnimation;
    }

    public Animation<TextureRegion> getAbsorbAnimation() {
        return absorbAnimation;
    }*/

    @Override
    public void act(float delta)
    {
        stateMachine.update(delta);
        Vector2 velocity = body.getLinearVelocity();

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            float brakeForce = 10f;
            body.applyForce(-velocity.x * brakeForce, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public void detach()
    {
        body.destroyFixture(fixture);
        body.destroyFixture(absorbRFixture);
        body.destroyFixture(absorbLFixture);
        world.destroyBody(body);
    }
}
