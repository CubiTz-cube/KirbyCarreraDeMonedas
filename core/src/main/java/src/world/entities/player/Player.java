package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.net.packets.Packet;
import src.screens.GameScreen;
import src.utils.Box2dUtils;
import src.utils.FrontRayCastCallback;
import src.utils.constants.CollisionFilters;
import src.utils.constants.PlayerControl;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.items.CoinOdsPoint;
import src.world.entities.items.PowerItem;
import src.world.entities.mirror.Mirror;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.entities.player.powers.PowerUp;
import src.world.entities.player.states.*;
import src.world.statics.Lava;
import src.world.statics.Spike;

import java.util.ArrayList;
import java.util.Random;

public class Player extends PlayerCommon {
    public Integer coinDrop = 3;
    public static final Integer DEFAULT_COIN_DROP = 3;

    public PowerUp.Type powerAbsorded;

    public final GameScreen game;

    private final Color color;
    private Float invencibleTime;
    private Boolean invencible;

    private Random random;

    public Player(World world, Float x, Float y, AssetManager assetManager, GameScreen game, Color color) {
        super(world, x,y, assetManager, -1);
        this.game = game;
        this.color = color;

        setColor(this.color);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.PLAYER;
        filter.maskBits = (short)~CollisionFilters.OTHERPLAYER;
        fixture.setFilterData(filter);

        initStates();
        setCurrentState(StateType.IDLE);

        invencibleTime = 0f;
        invencible = false;

        random = new Random();
        //setCurrentPowerUp(PowerUp.Type.WHEEL);
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

    public Boolean isEnemyAbsorb(){
        return powerAbsorded == null;
    }

    public void consumeEnemy() {
        if (powerAbsorded == null) return;
        PowerUp.Type powerType = powerAbsorded;
        powerAbsorded = null;
        setCurrentState(PlayerCommon.StateType.IDLE);
        if (powerType != PowerUp.Type.NONE) playSound(SoundType.POWER);
        setCurrentPowerUp(powerType);
    }

    @Override
    public void setAnimation(AnimationType animationType) {
        super.setAnimation(animationType);
        if (game != null) game.sendPacket(Packet.actOtherPlayer(-1, getCurrentAnimationType(), isFlipX(), getCurrentStateType(), getCurrentpowerUptype()));
    }

    @Override
    public void setFlipX(Boolean flipX) {
        super.setFlipX(flipX);
        if (game != null) game.sendPacket(Packet.actOtherPlayer(-1, getCurrentAnimationType(), isFlipX(), getCurrentStateType(), getCurrentpowerUptype()));
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        if (game != null) game.sendPacket(Packet.actEntityColor(-1, getColor().r, getColor().g, getColor().b, getColor().a));
    }

    public void setInvencible(Float time) {
        invencible = true;
        setColor(Color.GOLD);
        invencibleTime = time;
    }

    public Boolean isInvencible(){
        return invencible;
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

        if (fixture.getUserData() instanceof Enemy enemy){
            game.sendPacket(Packet.actDamageEnemy(enemy.getId(), 0, force.x, force.y, 1));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (invencibleTime > 0) invencibleTime -= delta;
        else if (invencible){
            setColor(color);
            invencible = false;
        }

        Vector2 velocity = body.getLinearVelocity();
        if (getCurrentStateType() == StateType.DASH || getCurrentStateType() == StateType.STUN) return;
        if (!Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            body.applyForce(-velocity.x * brakeForce * delta, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public void lossPoints(Integer amount){
        int coins;
        Integer score = game.getScore();
        if (score == null) return;

        if (score > amount) {
            coins = amount;
            game.setScore(score - coins);
        } else {
            coins = score;
            game.setScore(0);
        }

        for (int i = 0; i<coins;i++){
            game.addEntity(Type.COIN, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
        }
    }

    public void dropPower(){
        if (currentpowerUptype == null) return;
        switch (currentpowerUptype){
            case BOMB -> game.addEntity(Type.POWERBOMB, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
            case WHEEL -> game.addEntity(Type.POWERWHEEL, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
            case SWORD -> game.addEntity(Type.POWERSWORD, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
        }
        setCurrentPowerUp(null);
    }

    public void doAction(){
        if (currentpowerUptype != null) {
            PowerUp power = getCurrentPowerUp();
            if (getCurrentStateType() == StateType.IDLE || getCurrentStateType() == StateType.WALK ) power.actionIdle();
            else if (getCurrentStateType() == StateType.RUN) power.actionMove();
            else if (getCurrentStateType() == StateType.JUMP || getCurrentStateType() == StateType.FALL) power.actionAir();
        }
        else if (powerAbsorded == null) setCurrentState(Player.StateType.ABSORB);
        else setCurrentState(Player.StateType.STAR);
    }

    public void throwEntity(Entity.Type type, Float impulse){
        float linearX = Math.abs(body.getLinearVelocity().x);
        game.addEntity(type,
            body.getPosition().add(isFlipX() ? -1.2f : 1.2f,0),
            new Vector2((isFlipX() ? -impulse - linearX : impulse + linearX),0),
            isFlipX()
        );
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Enemy enemy) {
            if (getCurrentStateType() == StateType.ABSORB){
                powerAbsorded = enemy.getPowerType();
                game.removeEntity(enemy.getId());
                setCurrentState(Player.StateType.IDLE);
                return;
            }

            if (getCurrentStateType() == StateType.DASH && enemy.getCurrentStateType() != Enemy.StateType.DAMAGE){
                Box2dUtils.knockbackBody(body, enemy.getBody(), 5f);
                game.actDamageEnemy(enemy.getId(), body, 2, 2f);
                setInvencible(0.5f);
                setCurrentState(StateType.FALL);
                return;
            }

            if (getCurrentStateType() == StateType.STUN || invencible || enemy.getCurrentStateType() == Enemy.StateType.DAMAGE) return;
            setCurrentState(Player.StateType.STUN);
            playSound(SoundType.NORMALDAMAGE);
            Box2dUtils.knockbackBody(body, enemy.getBody(), 10f);

        } else if (actor instanceof Mirror m) {
            game.threadSecureWorld.addModification(() -> {
                game.playMinigame();
                game.randomMirror(m.getId());
            });
        } else if (actor instanceof PowerItem power){
            if (getCurrentStateType() == StateType.STUN || invencible || getCurrentStateType() != StateType.ABSORB) return;
            playSound(SoundType.POWER);
            powerAbsorded = power.getPowerType();
            setCurrentState(Player.StateType.IDLE);
            power.despawn();
        } else if (actor instanceof CoinOdsPoint coin){
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            playSound(SoundType.SCORE2);
            coin.despawn();
            game.setScore(game.getScore() + 1);
        } else if (actor instanceof Spike spike) {
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            stunTime = 0.5f;
            setCurrentState(Player.StateType.STUN);
            playSound(SoundType.NORMALDAMAGE);
            Box2dUtils.knockbackBody(body, spike.getBody(), 10f);
        } else if (actor instanceof Lava lava) {
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            stunTime = 0.5f;
            setCurrentState(Player.StateType.STUN);
            playSound(SoundType.FIREDAMAGE);
            Box2dUtils.knockbackBody(body, lava.getBody(), 10f);
        }else if (actor instanceof OtherPlayer other){
            if (getCurrentStateType() == StateType.STUN || invencible || other.getCurrentStateType() != StateType.DASH) return;
            setCurrentState(Player.StateType.STUN);
            Box2dUtils.knockbackBody(body, other.getBody(), 10f);
        }
    }
}
