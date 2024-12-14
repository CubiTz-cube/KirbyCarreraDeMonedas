package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.net.packets.Packet;
import src.screens.GameScreen;
import src.utils.constants.CollisionFilters;
import src.utils.constants.PlayerControl;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.objects.CoinOdsPoint;
import src.world.entities.mirror.Mirror;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.entities.player.powers.PowerUp;
import src.world.entities.player.powers.PowerWheel;
import src.world.entities.player.states.*;
import src.world.entities.projectiles.Projectil;
import src.world.statics.Lava;
import src.world.statics.Spike;

import java.util.Random;

public class Player extends PlayerCommon {

    public Enemy enemyAbsorded;

    public final GameScreen game;

    private final Color color;
    private Float invencibleTime;
    private Boolean invencible;

    public Player(World world, Rectangle shape, AssetManager assetManager, GameScreen game, Color color) {
        super(world, shape, assetManager, -1);
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

    public void consumeEnemy() {
        if (enemyAbsorded == null) return;
        PowerUp.Type powerType = enemyAbsorded.getPowerUp();
        enemyAbsorded = null;
        setCurrentState(PlayerCommon.StateType.IDLE);
        if (powerType != null) playSound(SoundType.POWER);
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
        Random random = new Random();

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
        Vector2 pushDirection = body.getPosition().cpy().sub(actor.getBody().getPosition()).nor();
        if (actor instanceof Enemy enemy) {
            if (getCurrentStateType() == StateType.ABSORB){
                enemyAbsorded = enemy;
                game.removeEntity(enemy.getId());
                setCurrentState(Player.StateType.IDLE);
                return;
            }

            if (getCurrentStateType() == StateType.DASH && enemy.getCurrentStateType() != Enemy.StateType.DAMAGE){
                enemy.takeDamage(1);
                body.setLinearVelocity(0,0);
                body.applyLinearImpulse(pushDirection.scl(5f), body.getWorldCenter(), true);
                body.applyLinearImpulse(0,5f, body.getWorldCenter().x, body.getWorldCenter().y, true);
                enemy.getBody().setLinearVelocity(0,0);
                enemy.getBody().applyLinearImpulse(pushDirection.scl(-2f), body.getWorldCenter(), true);
                enemy.getBody().applyLinearImpulse(0,2f, body.getWorldCenter().x, body.getWorldCenter().y, true);
                setCurrentState(StateType.FALL);
                return;
            }

            if (getCurrentStateType() == StateType.STUN || invencible) return;
            setCurrentState(Player.StateType.STUN);
            playSound(SoundType.NORMALDAMAGE);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);

        } else if (actor instanceof Mirror m) {
            game.threadSecureWorld.addModification(() -> {
                game.playMinigame();
                game.randomMirror(m.getId());
            });
        } else if (actor instanceof Projectil projectil) {
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            setCurrentState(Player.StateType.STUN);
            playSound(SoundType.NORMALDAMAGE);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);
            projectil.despawn();
        } else if (actor instanceof CoinOdsPoint coin){
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            playSound(SoundType.SCORE2);
            coin.despawn();
            game.setScore(game.getScore() + 1);
        } else if (actor instanceof Spike) {
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            stunTime = 0.5f;
            setCurrentState(Player.StateType.STUN);
            playSound(SoundType.NORMALDAMAGE);
            body.setLinearVelocity(0,0);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);
        } else if (actor instanceof Lava) {
            if (getCurrentStateType() == StateType.STUN || invencible) return;
            stunTime = 0.5f;
            setCurrentState(Player.StateType.STUN);
            playSound(SoundType.FIREDAMAGE);
            body.setLinearVelocity(0,0);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);
        }else if (actor instanceof OtherPlayer other){
            if (getCurrentStateType() == StateType.STUN || invencible || other.getCurrentStateType() != StateType.DASH) return;
            setCurrentState(Player.StateType.STUN);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);
        }
    }
}
