package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.world.entities.enemies.bomb.BombEnemy;
import src.world.entities.enemies.dragon.DragonEnemy;
import src.world.entities.enemies.fly.FlyEnemy;
import src.world.entities.enemies.wheel.WheelEnemy;
import src.world.entities.items.CoinOdsPoint;
import src.world.entities.items.PowerItem;
import src.world.entities.blocks.BreakBlock;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;
import src.world.entities.enemies.sword.SwordEnemy;
import src.world.entities.blocks.FallBlock;
import src.world.entities.mirror.Mirror;
import src.world.entities.player.powers.PowerUp;
import src.world.entities.projectiles.*;
import src.world.entities.projectiles.enemyProyectiles.IceEnemyProyectile;
import src.world.entities.projectiles.enemyProyectiles.SwordEnemyProyectile;

public class EntityFactory {
    private final GameScreen game;

    public EntityFactory(GameScreen game){
        this.game = game;
    }

    public Entity create(Entity.Type type, World world, Vector2 position, Integer id){
        AssetManager assetManager = game.main.getAssetManager();
        return switch (type) {
            case BASIC -> new BasicEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case SLEEPY -> new SleepingEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case SWORD -> new SwordEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case FLYBUG -> new FlyEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case WHEEL -> new WheelEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case BOMBER -> new BombEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case DRAGON -> new DragonEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case MIRROR -> new Mirror(world, new Rectangle(position.x, position.y, 2f, 2f), assetManager, id);
            case CLOUD -> new CloudProyectile(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case STAR -> new StarProyectile(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case BOMB -> new BombProyectile(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case ICE -> new IceEnemyProyectile(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case BOMBEXPLOSION -> new BombExplosionProyectile(world, new Rectangle(position.x, position.y, 12f, 12f), assetManager, id, game);
            case SWORDPROPLAYER -> new SwordProyectile(world, new Rectangle(position.x, position.y, 1f, 1.2f), assetManager, id, Entity.Type.SWORDPROPLAYER, game, assetManager.get("world/particles/kirbySwordParticle.png"));
            case SWORDRUNPROPLAYER -> new SwordRunProyectile(world, new Rectangle(position.x, position.y, 1f, 1.2f), assetManager, id, Entity.Type.SWORDRUNPROPLAYER, game);
            case SWORDPROENEMY -> new SwordEnemyProyectile(world, new Rectangle(position.x, position.y, 1f, 1.2f), assetManager, id, Entity.Type.SWORDPROENEMY, game, assetManager.get("world/particles/swordParticle.png"));
            case COIN -> new CoinOdsPoint(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case POWERSWORD -> new PowerItem(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game, PowerUp.Type.SWORD);
            case POWERWHEEL -> new PowerItem(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game, PowerUp.Type.WHEEL);
            case POWERBOMB -> new PowerItem(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game, PowerUp.Type.BOMB);
            case BREAKBLOCK -> new BreakBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case FALLBLOCK -> new FallBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case PLATFORMXR -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMXR, new Vector2(1,0));
            case PLATFORMXL -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMXL, new Vector2(-1,0));
            case PLATFORMYU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMYU, new Vector2(0,1));
            case PLATFORMRU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMRU, new Vector2(1,1).nor());
            case PLATOFRMRD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATOFRMRD, new Vector2(1,-1).nor());
            case PLATFORMLU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMLU, new Vector2(-1,1).nor());
            case PLATFORMLD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.PLATFORMLD, new Vector2(-1,-1).nor());
            default -> null;
        };

    }
}
