package src.world.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.ActorBox2d;
import src.world.entities.enemies.*;
import src.world.entities.enemies.sleeping.SleepingEnemy;
import src.world.entities.otherPlayer.OtherPlayer;

public class EntityFactory{
    private final Main game;
    public enum Type{
        OTHERPLAYER,
        BASIC,
        SLEEPY

    }

    public EntityFactory(Main game){
        this.game = game;
    }

    public ActorBox2d create(EntityFactory.Type actor, World world, Vector2 position, Integer id){
        return switch (actor) {
            case OTHERPLAYER -> new OtherPlayer(world, game.getAssetManager().get("yoshi.jpg"), new Rectangle(position.x, position.y, 1f, 1f), id);
            case BASIC -> new BasicEnemy(world, game.getAssetManager().get("perro.jpg"), new Rectangle(position.x, position.y, 1f, 1f), id);
            case SLEEPY -> new SleepingEnemy(world, game.getAssetManager().get("yozhi.jpg"), new Rectangle(position.x, position.y, 1f, 1f), id);
            default -> null;
        };
    }
}
