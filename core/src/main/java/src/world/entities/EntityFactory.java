package src.world.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.entities.breakBlocks.BreakBlock;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;
import src.world.entities.mirror.Mirror;

public class EntityFactory {
    private final Main main;

    public EntityFactory(Main main){
        this.main = main;
    }

    public Entity create(Entity.Type type, World world, Vector2 position, Integer id){
        return switch (type) {
            case BASIC -> new BasicEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), main.getAssetManager(), id);
            case SLEEPY -> new SleepingEnemy(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id);
            case MIRROR -> new Mirror(world, new Rectangle(position.x, position.y, 2f, 2f), main.getAssetManager(), id);
            case BREAKBLOCK -> new BreakBlock(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id);
            case PLATFORMXR -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, new Vector2(1,0));
            case PLATFORMXL -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, new Vector2(-1,0));
            case PLATFORMYU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, new Vector2(0,1));
            case PLAYFORMRU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, new Vector2(1,1));
            case PLATOFRMRD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, new Vector2(1,-1));
            case PLATFORMLU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, new Vector2(-1,1));
            case PLATFORMLD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, new Vector2(-1,-1));
            default -> null;
        };

    }
}
