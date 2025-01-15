package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.world.entities.blocks.SwordBreakBlock;
import src.world.entities.blocks.BombBreakBlock;
import src.world.entities.enemies.bomb.BombEnemy;
import src.world.entities.enemies.dragon.DragonEnemy;
import src.world.entities.enemies.fly.FlyEnemy;
import src.world.entities.enemies.turret.TurretEnemy;
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
import src.world.entities.projectiles.enemyProyectiles.IceEnemyProyectil;
import src.world.entities.projectiles.enemyProyectiles.SwordEnemyProyectil;
import src.world.entities.projectiles.enemyProyectiles.TurretEnemyProyectil;

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
            case TURRET -> new TurretEnemy(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), assetManager, id, game);
            case MIRROR -> new Mirror(world, new Rectangle(position.x, position.y, 2f, 2f), assetManager, id);
            case CLOUD -> new CloudProyectil(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case STAR -> new StarProyectil(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case BOMB -> new BombProyectil(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case ICE -> new IceEnemyProyectil(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case BOMBEXPLOSION -> new BombExplosionProyectil(world, new Rectangle(position.x, position.y, 12f, 12f), assetManager, id, game);
            case SWORDPROPLAYER -> new SwordProyectil(world, new Rectangle(position.x, position.y, 1f, 1.2f), assetManager, id, Entity.Type.SWORDPROPLAYER, game, assetManager.get("world/particles/kirbySwordParticle.png"));
            case SWORDRUNPROPLAYER -> new SwordRunProyectil(world, new Rectangle(position.x, position.y, 1f, 1.2f), assetManager, id, Entity.Type.SWORDRUNPROPLAYER, game);
            case SWORDPROENEMY -> new SwordEnemyProyectil(world, new Rectangle(position.x, position.y, 1f, 1.2f), assetManager, id, Entity.Type.SWORDPROENEMY, game, assetManager.get("world/particles/swordParticle.png"));
            case TURRETPROENEMY -> new TurretEnemyProyectil(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.TURRETPROENEMY, game, assetManager.get("world/particles/turretParticle.png"));
            case COIN -> new CoinOdsPoint(world, new Rectangle(position.x, position.y, 0.75f, 0.75f), assetManager, id, game);
            case POWERSWORD -> new PowerItem(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game, PowerUp.Type.SWORD, assetManager.get("world/entities/powers/swordPowerItem.png"), 0.1f, 8);
            case POWERWHEEL -> new PowerItem(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game, PowerUp.Type.WHEEL, assetManager.get("world/entities/powers/wheelPowerItem.png"), 0.2f, 2);
            case POWERBOMB -> new PowerItem(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game, PowerUp.Type.BOMB, assetManager.get("world/entities/powers/bombPowerItem.png"), 0.15f, 4);
            case BREAKBLOCK -> new BreakBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case SWORDBREAKBLOCK -> new SwordBreakBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.SWORDBREAKBLOCK, game);
            case BOMBBREAKBLOCK -> new BombBreakBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, Entity.Type.BOMBBREAKBLOCK, game);
            case FALLBLOCK -> new FallBlock(world, new Rectangle(position.x, position.y, 1f, 1f), assetManager, id, game);
            case PLATFORMXR -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATFORMXR, 1,0);
            case PLATFORMXL -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATFORMXL, -1,0);
            case PLATFORMYU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATFORMYU, 0,1);
            case PLATFORMYD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATFORMYU, 0,-1);
            case PLATFORMRU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATFORMRU, 1,1);
            case PLATOFRMRD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATOFRMRD, 1,-1);
            case PLATFORMLU -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATFORMLU, -1,1);
            case PLATFORMLD -> new MovingPlatform(world, new Rectangle(position.x, position.y, 3f, 0.5f), assetManager, id, Entity.Type.PLATFORMLD, -1,-1);
            default -> null;
        };

    }
}
