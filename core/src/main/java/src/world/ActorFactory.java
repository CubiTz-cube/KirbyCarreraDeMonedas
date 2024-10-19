package src.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.main.Main;
import src.world.entities.player.Player;
import src.world.floors.Floor;

public class ActorFactory {
    private final Main game;
    public enum ActorType{
        PLAYER,
        ENEMY,
        FLOOR
    }

    public ActorFactory(Main game){
        this.game = game;
    }

    public Actor createActor(ActorType actor, World world, Rectangle shape){
        switch (actor){
            case PLAYER:
                return new Player(world, game.getAssetManager().get("yoshi.jpg"), shape);
            case FLOOR:
                return new Floor(world, game.getAssetManager().get("floor.png"), shape);
            default:
                return null;
        }
    }
}
