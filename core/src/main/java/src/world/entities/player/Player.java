package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.main.Main;
import src.screens.GameScreen;
import src.utils.CollisionFilters;
import src.utils.variables.PlayerControl;
import src.world.ActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.objects.CoinOdsPoint;
import src.world.entities.mirror.Mirror;
import src.world.entities.player.powers.PowerUp;
import src.world.entities.player.states.*;
import src.world.entities.projectiles.Projectil;

import java.util.Random;

public class Player extends PlayerCommon {
    public float speed = 12;
    public float maxSpeed = 6;

    public static final float WALK_SPEED = 10f;
    public static final float WALK_MAX_SPEED = 5f;
    public static final float RUN_SPEED = 14f;
    public static final float RUN_MAX_SPEED = 6.5f;
    public static final float MAX_JUMP_TIME = 0.3f;
    public static final float JUMP_IMPULSE = 9f;
    public static final float JUMP_INAIR = 16f; // Se multiplica por deltaTime
    public static final float FLY_IMPULSE = 6f;
    public static final float DASH_IMPULSE = 15f;
    public static final float ABSORB_FORCE = 12f;
    public static final float BRAKE_FORCE = 280f;

    private Boolean changeAnimation;

    public Enemy enemyAbsorded;

    public final GameScreen game;

    private Float invencibleTime;
    private Boolean invencible;

    public Player(World world, Rectangle shape, AssetManager assetManager, GameScreen game) {
        super(world, shape, assetManager, -1);
        this.game = game;
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

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_PLAYER;
        filter.maskBits = ~CollisionFilters.MASK_OTHERPLAYER;
        fixture.setFilterData(filter);

        setSpritePosModification(0f, getHeight()/4);

        initStates();
        setCurrentState(StateType.IDLE);

        changeAnimation = false;
        invencibleTime = 0f;
        invencible = false;
    }

    private void initStates(){
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
        starState = new StarState(this);
    }

    public void consumeEnemy() {
        if (enemyAbsorded == null) return;
        PowerUp.Type powerType = enemyAbsorded.getPowerUp();
        enemyAbsorded = null;
        setCurrentState(PlayerCommon.StateType.IDLE);
        setCurrentPowerUp(powerType);
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

    public void setInvencible(Float time) {
        invencible = true;
        sprite.setColor(Color.GOLD);
        invencibleTime = time;
    }

    public Boolean isInvencible(){
        return invencible;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (invencibleTime > 0) invencibleTime -= delta;
        else if (invencible){
            sprite.setColor(Color.WHITE);
            invencible = false;
        }
        Vector2 velocity = body.getLinearVelocity();

        if (getCurrentStateType() == StateType.DASH || getCurrentStateType() == StateType.STUN) return;
        if (!Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            body.applyForce(-velocity.x * Player.BRAKE_FORCE * delta, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public void lossPoints(Integer amount){
        int coins;
        Random random = new Random();

        if (game.getScore() > amount) {
            coins = amount;
            game.setScore(game.getScore() - coins);
        } else {
            coins = game.getScore();
            game.setScore(0);
        }

        for (int i = 0; i<coins;i++){
            game.addEntity(Type.COIN, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
        }
    }

    public void doAction(){
        if (currentpowerUptype != null) {
            PowerUp power = getCurrentPowerUp();
            if (getCurrentStateType() == StateType.IDLE || getCurrentStateType() == StateType.WALK ) power.actionIdle();
            else if (getCurrentStateType() == StateType.RUN) power.actionMove();
            else if (getCurrentStateType() == StateType.JUMP || getCurrentStateType() == StateType.FALL) power.actionAir();
        }
        else if (enemyAbsorded == null) setCurrentState(Player.StateType.ABSORB);
        else setCurrentState(Player.StateType.STAR);
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Enemy enemy) {
            if (getCurrentStateType() == StateType.ABSORB){
                enemyAbsorded = enemy;
                game.removeEntity(enemy.getId());
                setCurrentState(Player.StateType.IDLE);
                return;
            }

            Vector2 pushDirection = body.getPosition().cpy().sub(actor.getBody().getPosition()).nor();
            if (getCurrentStateType() == StateType.DASH && enemy.getCurrentStateType() != Enemy.StateType.DAMAGE){
                enemy.takeDamage(1);
                body.setLinearVelocity(0,0);
                body.applyLinearImpulse(pushDirection.scl(5f), body.getWorldCenter(), true);
                body.applyLinearImpulse(0,5f, body.getWorldCenter().x, body.getWorldCenter().y, true);
                setCurrentState(StateType.FALL);
                return;
            }

            if (getCurrentStateType() == StateType.STUN || invencible) return;
            setCurrentState(Player.StateType.STUN);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);
            lossPoints(3);

        } else if (actor instanceof Mirror) {
            game.threadSecureWorld.addModification(() -> {
                game.getPlayer().getBody().setTransform(game.lobbyPlayer.x, game.lobbyPlayer.y, 0);
                game.main.changeScreen(Main.Screens.MINIDUCK);
                game.randomMirror();
            });
        } else if (actor instanceof Projectil) {
            Vector2 pushDirection = body.getPosition().cpy().sub(actor.getBody().getPosition()).nor();
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            setCurrentState(Player.StateType.STUN);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);
            lossPoints(3);
        } else if (actor instanceof CoinOdsPoint coin){
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            game.removeEntity(coin.getId());
            game.setScore(game.getScore() + 1);
        }
    }
}
