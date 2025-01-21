package src.world.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.game.GameScreen;
import src.utils.stateMachine.StateMachine;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.player.powers.PowerUp;
import src.world.statics.Lava;

public abstract class Enemy extends Entity {
    private Float actCrono;
    protected PowerUp.Type powerUp;

    protected StateMachine stateMachine;
    public enum StateType {
        IDLE,
        WALK,
        ATTACK,
        DAMAGE
    }
    private StateType currentStateType;
    protected StateEnemy<?> idleState;
    protected StateEnemy<?> walkState;
    protected StateEnemy<?> attackState;
    protected StateEnemy<?> damageState;
    private Boolean changeState;

    public GameScreen game;

    public Float speed;
    private Integer live;

    private Sound damageSound;
    private Sound deadSound;

    public Enemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game, Type type, PowerUp.Type powerUp, Integer live) {
        super(world, shape, assetManager,id, type);
        this.game = game;

        this.actCrono = 0f;
        this.powerUp = powerUp;
        stateMachine = new StateMachine();
        speed = 3f;
        changeState = false;
        this.live = live;

        damageSound = assetManager.get("sound/enemy/enemyDamage.wav");
        deadSound = assetManager.get("sound/enemy/enemyDead.wav");
    }

    public void setActCrono(Float actCrono) {
        this.actCrono = actCrono;
    }

    public PowerUp.Type getPowerType() {
        return powerUp;
    }

    public Float getActCrono() {
        return actCrono;
    }

    public void setState(StateType state){
        currentStateType = state;
        changeState = true;
        switch (state){
            case IDLE -> stateMachine.setState(idleState);
            case WALK -> stateMachine.setState(walkState);
            case ATTACK -> stateMachine.setState(attackState);
            case DAMAGE -> stateMachine.setState(damageState);
        }
    }

    public StateType getCurrentStateType() {
        return currentStateType;
    }

    public Boolean checkChangeState() {
        if (changeState) {
            changeState = false;
            return true;
        }
        return false;
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
        game.addEntityNoPacket(type,
            body.getPosition().add(isFlipX() ? -1.2f : 1.2f,0),
            new Vector2((isFlipX() ? -impulseX - linearX : impulseX + linearX),impulseY),
            isFlipX()
        );
    }

    public void takeDamage(Integer damage) {
        if (damage <= 0) return;
        live -= damage;
        if (live > 0) game.playProximitySound(damageSound, body.getPosition(),30);
        else game.playProximitySound(deadSound, body.getPosition(),30);
        setState(StateType.DAMAGE);
    }

    public Boolean isDead(){
        return live <= 0;
    }

    @Override
    public void act(float delta) {
        actCrono += delta;
        stateMachine.update(delta);
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Lava lava){
            takeDamage(99);
        }
    }
}
