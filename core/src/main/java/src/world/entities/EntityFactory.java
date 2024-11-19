package src.world.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.entities.breakBlocks.BreakBlock;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;
import src.world.entities.enemies.sword.SwordEnemy;
import src.world.entities.mirror.Mirror;
import src.world.entities.projectiles.Cloud;

public class EntityFactory {
    private final Main main;

    public EntityFactory(Main main){
        this.main = main;
    }

    public Entity create(Entity.Type type, World world, Vector2 position, Integer id){
        return switch (type) {
            case BASIC -> new BasicEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), main.getAssetManager(), id);
            case SLEEPY -> new SleepingEnemy(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id);
            case SWORD -> new SwordEnemy(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id);
            case MIRROR -> new Mirror(world, new Rectangle(position.x, position.y, 2f, 2f), main.getAssetManager(), id);
            case CLOUD -> new Cloud(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id);
            case BREAKBLOCK -> new BreakBlock(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id);
            case PLATFORMXR -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, Entity.Type.PLATFORMXR, new Vector2(1,0));
            case PLATFORMXL -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, Entity.Type.PLATFORMXL, new Vector2(-1,0));
            case PLATFORMYU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, Entity.Type.PLATFORMYU, new Vector2(0,1));
            case PLATFORMRU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, Entity.Type.PLATFORMRU, new Vector2(1,1));
            case PLATOFRMRD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, Entity.Type.PLATOFRMRD, new Vector2(1,-1));
            case PLATFORMLU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, Entity.Type.PLATFORMLU, new Vector2(-1,1));
            case PLATFORMLD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), main.getAssetManager(), id, Entity.Type.PLATFORMLD, new Vector2(-1,-1));
            default -> null;
        };

    }
}
