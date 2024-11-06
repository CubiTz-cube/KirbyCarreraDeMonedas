package src.world.entities.enemies;

import com.badlogic.gdx.physics.box2d.World;
import src.utils.stateMachine.StateMachine;
import src.world.entities.Entity;
import src.world.player.powers.PowerUp;

public abstract class Enemy extends Entity {
    public enum Type{
        BASIC,
        SLEEPY
    }
    protected Type type;
    private Float actCrono;
    protected PowerUp.Type powerUp;

    protected StateMachine stateMachine;
    public enum State{
        IDLE,
        WALK,
        ATTACK,
        DAMAGE
    }
    private State state;
    protected StateEnemy idleState;
    protected StateEnemy walkState;
    protected StateEnemy attackState;
    protected StateEnemy damageState;
    private Boolean changeState;

    public Float speed;

    public Enemy(World world, Integer id) {
        super(world, id);
        this.actCrono = 0f;
        powerUp = PowerUp.Type.NULL;
        stateMachine = new StateMachine();
        speed = 3f;
        changeState = false;
    }

    public void setActCrono(Float actCrono) {
        this.actCrono = actCrono;
    }

    public PowerUp.Type getPowerUp() {
        return powerUp;
    }

    public Type getType() {
        return type;
    }

    public Float getActCrono() {
        return actCrono;
    }

    public void setState(State state){
        this.state = state;
        changeState = true;
        switch (state){
            case IDLE -> stateMachine.setState(idleState);
            case WALK -> stateMachine.setState(walkState);
            case ATTACK -> stateMachine.setState(attackState);
            case DAMAGE -> stateMachine.setState(damageState);
        }
    }

    public Boolean checkChangeState() {
        if (changeState) {
            changeState = false;
            return true;
        }
        return false;
    }

    public State getState() {
        return state;
    }

    @Override
    public void act(float delta) {
        actCrono += delta;
        stateMachine.update(delta);
    }
}
