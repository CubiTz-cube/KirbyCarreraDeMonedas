package src.world.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.constants.CollisionFilters;
import src.utils.stateMachine.StateMachine;
import src.world.entities.Entity;
import src.world.entities.player.powers.PowerUp;

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

    public Enemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game, Type type, PowerUp.Type powerUp, Integer live) {
        super(world, shape, assetManager,id, type);
        this.game = game;

        this.actCrono = 0f;
        this.powerUp = powerUp;
        stateMachine = new StateMachine();
        speed = 3f;
        changeState = false;
        this.live = live;
    }

    public void setActCrono(Float actCrono) {
        this.actCrono = actCrono;
    }

    public PowerUp.Type getPowerUp() {
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

    public void takeDamage(Integer damage) {
        live -= damage;
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
}
