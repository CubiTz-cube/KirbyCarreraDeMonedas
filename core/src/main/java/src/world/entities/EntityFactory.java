package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.world.entities.objects.CoinOdsPoint;
import src.world.entities.staticEntity.MovingPlatform;
import src.world.entities.staticEntity.blocks.BreakBlock;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;
import src.world.entities.enemies.sword.SwordEnemy;
import src.world.entities.staticEntity.blocks.FallBlock;
import src.world.entities.staticEntity.mirror.Mirror;
import src.world.entities.projectiles.Cloud;
import src.world.entities.projectiles.Star;

public class EntityFactory {
    private final GameScreen game;

    public EntityFactory(GameScreen game){
        this.game = game;
    }

    public Entity create(Entity.Type type, World world, Vector2 position, Integer id){
        AssetManager assetManager = game.main.getAssetManager();
        return switch (type) {
            case BASIC -> new BasicEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case SLEEPY -> new SleepingEnemy(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case SWORD -> new SwordEnemy(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case MIRROR -> new Mirror(world, new Rectangle(position.x, position.y, 2f, 2f), assetManager, id);
            case CLOUD -> new Cloud(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case STAR -> new Star(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case COIN -> new CoinOdsPoint(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id);
            case BREAKBLOCK -> new BreakBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id);
            case FALLBLOCK -> new FallBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id);
            case PLATFORMXR -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMXR, new Vector2(1,0));
            case PLATFORMXL -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMXL, new Vector2(-1,0));
            case PLATFORMYU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMYU, new Vector2(0,1));
            case PLATFORMRU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMRU, new Vector2(1,1));
            case PLATOFRMRD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATOFRMRD, new Vector2(1,-1));
            case PLATFORMLU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMLU, new Vector2(-1,1));
            case PLATFORMLD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMLD, new Vector2(-1,-1));
            default -> null;
        };

    }
}
