package src.world.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;
import src.world.entities.mirror.Mirror;
import src.world.entities.otherPlayer.OtherPlayer;

public class EntityFactory {
    private final Main main;

    public EntityFactory(Main main){
        this.main = main;
    }

    public Entity create(Entity.Type type, World world, Vector2 position, Integer id){
        return switch (type) {
            case BASIC -> new BasicEnemy(world, main.getAssetManager(), new Rectangle(position.x, position.y, 1f, 1f), id);
            case SLEEPY -> new SleepingEnemy(world, main.getAssetManager(), new Rectangle(position.x, position.y, 1f, 1f), id);
            case MIRROR -> new Mirror(world, main.getAssetManager(), new Rectangle(position.x, position.y, 2f, 2f), id);
            default -> null;
        };

    }
}
