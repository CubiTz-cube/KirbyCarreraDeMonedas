package src.world.entities.enemies;

import com.badlogic.gdx.physics.box2d.World;
import src.world.entities.Entity;
import src.world.player.powers.PowerUp;

public abstract class Enemy extends Entity {
    protected Float actCrono;
    protected PowerUp.Type powerUp;
    public enum Type{
        BASIC,
        SLEEPY
    }
    protected Type type;

    public Enemy(World world, Integer id, Float crono) {
        super(world, id);
        this.actCrono = crono;
        powerUp = PowerUp.Type.NULL;
    }

    public PowerUp.Type getPowerUp() {
        return powerUp;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void act(float delta) {
        actCrono += delta;
    }
}
