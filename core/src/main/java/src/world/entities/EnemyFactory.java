package src.world.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.entities.enemies.*;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class EnemyFactory{
    private final Main main;

    public EnemyFactory(Main main){
        this.main = main;
    }

    public Enemy create(Enemy.Type type, World world, Vector2 position, Integer id){
        return switch (type) {
            case BASIC -> new BasicEnemy(world, main.getAssetManager(), new Rectangle(position.x, position.y, 1f, 1f), id);
            case SLEEPY -> new SleepingEnemy(world, main.getAssetManager().get("yozhi.jpg"), new Rectangle(position.x, position.y, 1f, 1f), id);
            default -> null;
        };

    }
}
