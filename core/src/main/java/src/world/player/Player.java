package src.world.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.world.ActorBox2d;
import src.utils.stateMachine.*;
import src.world.SpriteActorBox2d;
import src.world.player.states.*;

import static src.utils.Constants.PIXELS_IN_METER;

public class Player extends SpriteActorBox2d {
    public float speed = 10;
    public float maxSpeed = 4;
    public static final float MAX_JUMP_TIME = 0.3f;
    public static final float JUMP_IMPULSE = 5f;
    public static final float JUMP_INAIR = 0.3f;
    public static final float DASH_IMPULSE = 10f;

    //private final Fixture sensorFixture;

    protected final StateMachine stateMachine;
    private final IdleState idleState;
    private final JumpState jumpState;
    private final FlyState flyState;
    private final WalkState walkState;
    private final FallState fallState;
    private final DashState dashState;
    private final DownState downState;
    private final RunState runState;

    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> idleAnimation;

    public Player(World world, AssetManager assetManager, Rectangle shape){
        super(world);
        setDebug(true);
        sprite = new Sprite(assetManager.get("yoshi.jpg", Texture.class));
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

        /*PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(shape.width/2, 0.2f, new Vector2(0, -0.4f), 0);
        FixtureDef sensorDef = new FixtureDef();
        sensorDef.shape = sensorShape;
        sensorDef.isSensor = true;
        sensorFixture = body.createFixture(sensorDef);
        sensorFixture.setUserData("playerBottomSensor");
        sensorShape.dispose();*/

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_PLAYER;
        filter.maskBits = ~CollisionFilters.MASK_PLAYER;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setSpritePosModification(0f, getHeight()/4);

        stateMachine = new StateMachine();
        idleState = new IdleState(stateMachine, this);
        jumpState = new JumpState(stateMachine, this);
        flyState = new FlyState(stateMachine, this);
        walkState = new WalkState(stateMachine, this);
        fallState = new FallState(stateMachine, this);
        dashState = new DashState(stateMachine, this);
        downState = new DownState(stateMachine, this);
        runState = new RunState(stateMachine, this);
        stateMachine.setState(idleState);

        Texture sheet = assetManager.get("world/entities/kirby/kirbyWalk.png");
        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / 10,
            sheet.getHeight());
        TextureRegion[] walkFrames = new TextureRegion[10];
        System.arraycopy(tmp[0], 0, walkFrames, 0, 10);
        walkAnimation = new Animation<>(0.05f, walkFrames);

        TextureRegion idleFrames = new TextureRegion((Texture) assetManager.get("yoshi.jpg"));
        idleAnimation = new Animation<>(0.05f, idleFrames);

        currentAnimation = idleAnimation;
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

    public DashState getDashState() {
        return dashState;
    }

    public DownState getDownState() {
        return downState;
    }

    public RunState getRunState() {
        return runState;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public Animation<TextureRegion> getWalkAnimation() {
        return walkAnimation;
    }

    @Override
    public void act(float delta) {
        stateMachine.update(delta);
    }

    public void detach(){
        body.destroyFixture(fixture);
        //body.destroyFixture(sensorFixture);
        world.destroyBody(body);
    }
}
