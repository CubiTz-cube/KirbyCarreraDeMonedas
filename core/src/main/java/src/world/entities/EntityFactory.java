package src.world.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.ActorBox2d;
import src.world.entities.enemies.*;
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

    public ActorBox2d create(EntityFactory.Type actor, World world, Rectangle shape, Integer id){
        return switch (actor) {
            case OTHERPLAYER -> new OtherPlayer(world, game.getAssetManager().get("yoshi.jpg"), shape, id);
            case BASIC -> new BasicEnemy(world, game.getAssetManager().get("perro.jpg"), shape, id);
            default -> null;
        };
    }
}
