package src.world.entities.enemies;

import com.badlogic.gdx.physics.box2d.Body;
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

    public Float speed;

    public Enemy(World world, Integer id, Float crono) {
        super(world, id);
        this.actCrono = crono;
        powerUp = PowerUp.Type.NULL;
        stateMachine = new StateMachine();
        speed = 3f;
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

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void resetActCrono() {
        actCrono = 0f;
    }

    @Override
    public void act(float delta) {
        actCrono += delta;
        stateMachine.update(delta);
    }
}
