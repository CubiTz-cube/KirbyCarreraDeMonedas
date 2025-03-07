package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.main.Modificable;
import src.net.packets.Packet;
import src.screens.game.GameScreen;
import src.utils.Box2dUtils;
import src.utils.FrontRayCastCallback;
import src.utils.SoundPicthUp;
import src.utils.constants.CollisionFilters;
import src.utils.constants.PlayerControl;
import src.utils.sound.SingleSoundManager;
import src.utils.sound.SoundManager;
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

    private final Random random;
    public enum ThrowDirection{
        LEFT,
        RIGHT,
        UP,
        DOWN,
    }

    public enum SoundType{
        AIRSHOT,
        ABSORB,
        DASH,
        FIREDAMAGE,
        ICEDAMAGE,
        NORMALDAMAGE,
        HEAVYFALL,
        ITEM,
        JUMP,
        POWER,
        SCORE,
        COIN,
        SLEEP,
        STAR,
        REMOVESELECT,
    }
    private Sound airShotSound;
    private Sound absorbSound;
    private Sound dashSound;
    private Sound fireDamageSound;
    private Sound iceDamageSound;
    private Sound normalDamageSound;
    private Sound heavyFallSound;
    private Sound itemSound;
    private Sound jumpSound;
    private Sound powerSound;
    private Sound scoreSound;
    private SoundPicthUp coinSound;
    private Sound sleepSound;
    private Sound starSound;
    private Sound removeSelectSound;

    private final String name;
    private final GlyphLayout layout;
    private final BitmapFont font;

    public Player(World world, Float x, Float y, AssetManager assetManager, GameScreen game, Color color) {
        super(world, x,y, assetManager, -1);
        name = game.main.getPlayerName();
        layout = new GlyphLayout();
        font = game.main.fonts.interNameFontSmall;

        this.game = game;
        this.color = color;

        setColor(this.color);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.PLAYER;
        filter.maskBits = (short)~CollisionFilters.OTHERPLAYER;
        fixture.setFilterData(filter);

        initStates();
        setCurrentState(StateType.IDLE);

        initSound();

        invencibleTime = 0f;
        invencible = false;

        random = new Random();
        setCurrentPowerUp(Modificable.PODER_INICIAL);
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

    private void initSound(){
        airShotSound = assetManager.get("sound/kirby/kirbyAirShot.wav");
        absorbSound = assetManager.get("sound/kirby/kirbyAbsorb.wav");
        dashSound = assetManager.get("sound/kirby/kirbyDash.wav");
        fireDamageSound = assetManager.get("sound/kirby/kirbyFireDamage.wav");
        iceDamageSound = assetManager.get("sound/kirby/kirbyIceDamage.wav");
        normalDamageSound = assetManager.get("sound/kirby/kirbyNormalDamage.wav");
        heavyFallSound = assetManager.get("sound/kirby/kirbyHeavyFall.wav");
        itemSound = assetManager.get("sound/kirby/kirbyItem.wav");
        jumpSound = assetManager.get("sound/kirby/kirbyJump.wav");
        powerSound = assetManager.get("sound/kirby/kirbyPower.wav");
        scoreSound = assetManager.get("sound/kirby/kirbyScore1.wav");
        coinSound = new SoundPicthUp(assetManager.get("sound/coin.wav"), 0.1f, 2f);
        sleepSound = assetManager.get("sound/kirby/kirbySleep.wav");
        starSound = assetManager.get("sound/kirby/kirbyStar.wav");
        removeSelectSound = assetManager.get("sound/kirby/kirbyRemovePower.wav");
    }

    public Boolean isEnemyAbsorb(){
        return powerAbsorded != null;
    }

    public PowerUp.Type getPowerAbsorded(){
        return powerAbsorded;
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

    private Sound getSound(SoundType type){
        return switch (type){
            case AIRSHOT -> airShotSound;
            case ABSORB -> absorbSound;
            case DASH -> dashSound;
            case FIREDAMAGE -> fireDamageSound;
            case ICEDAMAGE -> iceDamageSound;
            case NORMALDAMAGE -> normalDamageSound;
            case HEAVYFALL -> heavyFallSound;
            case ITEM -> itemSound;
            case JUMP -> jumpSound;
            case POWER -> powerSound;
            case SCORE -> scoreSound;
            case COIN -> coinSound;
            case SLEEP -> sleepSound;
            case STAR -> starSound;
            case REMOVESELECT -> removeSelectSound;
        };
    }

    public void playSound(SoundType type){
        SoundManager soundManager = SingleSoundManager.getInstance();

        if (getCurrentPowerUp() != null) {
            Sound sound = getCurrentPowerUp().getSound(type);
            if (sound != null) {
                soundManager.playSound(sound, 1f);
                return;
            }
        }

        soundManager.playSound(getSound(type), 1f);
    }

    public void stopSound(SoundType type){
        SoundManager soundManager = SingleSoundManager.getInstance();

        if (getCurrentPowerUp() != null) {
            Sound sound = getCurrentPowerUp().getSound(type);
            if (sound != null) {
                sound.stop();
                return;
            }
        }

        getSound(type).stop();
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
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        layout.setText(font, name);
        float textX = getX() + (sprite.getWidth() - layout.width) / 2;
        float textY = getY() + sprite.getHeight()/2 + layout.height / 2;
        font.draw(batch, layout, textX, textY);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (invencibleTime > 0) invencibleTime -= delta;
        else if (invencible){
            setColor(color);
            invencible = false;
        }

        coinSound.update(delta);

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
        if (currentpowerUptype == PowerUp.Type.NONE) return;
        switch (currentpowerUptype){
            case BOMB -> game.addEntity(Type.POWERBOMB, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
            case WHEEL -> game.addEntity(Type.POWERWHEEL, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
            case SWORD -> game.addEntity(Type.POWERSWORD, body.getPosition(), new Vector2(random.nextFloat(-3,3),random.nextFloat(-5,5)));
        }
        setCurrentPowerUp(PowerUp.Type.NONE);
    }

    public void doAction(){
        if (currentpowerUptype != PowerUp.Type.NONE) {
            PowerUp power = getCurrentPowerUp();
            if (getCurrentStateType() == StateType.IDLE || getCurrentStateType() == StateType.WALK ) power.actionIdle();
            else if (getCurrentStateType() == StateType.RUN) power.actionMove();
            else if (getCurrentStateType() == StateType.JUMP || getCurrentStateType() == StateType.FALL) power.actionAir();
        }
        else if (powerAbsorded == null) setCurrentState(Player.StateType.ABSORB);
        else setCurrentState(Player.StateType.STAR);
    }

    /**
     * Lanza hacia donde se mira desde la pocicion del enemigo una entidad con su velocidad en X mas
     * la velocidad del enemigo en X y Y
     * @param type Tipo de entidad a lanzar
     * @param impulseX Velocidad en X, se le suma la velocidad del enemigo en X
     * @param impulseY Velocidad en Y
     */
    public void throwEntity(Entity.Type type, Float impulseX, Float impulseY){
        float linearX = Math.abs(body.getLinearVelocity().x);
        game.addEntity(type,
            new Vector2( body.getPosition().add(isFlipX() ? -2.2f : 1.2f,-0.5f)),
            new Vector2((isFlipX() ? -impulseX - linearX : impulseX + linearX),impulseY),
            isFlipX()
        );
    }
    public void throwEntity(Entity.Type type, Float impulse, ThrowDirection direction){
        float linearX = Math.abs(body.getLinearVelocity().x);
        Vector2 spawnPos = new Vector2( switch (direction) {
            case LEFT -> body.getPosition().add(-2.2f,-0.5f);
            case RIGHT -> body.getPosition().add(1.2f,-0.5f);
            case UP -> body.getPosition().add(0,1.7f);
            case DOWN -> body.getPosition().add(0,-1.7f);
        });
        Vector2 impulseVector = switch (direction) {
            case LEFT -> new Vector2(-impulse + linearX,0);
            case RIGHT -> new Vector2(impulse + linearX,0);
            case UP -> new Vector2(0,impulse);
            case DOWN -> new Vector2(0,-impulse);
        };
        game.addEntity(type,
            spawnPos,
            impulseVector,
            direction == ThrowDirection.LEFT
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
                game.actDamageEnemy(enemy.getId(), body, dashDamage, 2f);
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
            powerAbsorded = power.getPowerType();
            setCurrentState(Player.StateType.IDLE);
            power.despawn();
        } else if (actor instanceof CoinOdsPoint coin){
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            playSound(SoundType.COIN);
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
