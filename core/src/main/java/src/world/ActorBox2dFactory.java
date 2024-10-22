package src.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.entities.player.Player;
import src.world.staticEntities.Floor;

public class ActorBox2dFactory {
    private final Main game;
    public enum ActorType{
        PLAYER,
        FLOOR
    }

    public ActorBox2dFactory(Main game){
        this.game = game;
    }

    /**
     * Crea un actor de tipo ActorBox2d
     *
     * @return ActorBox2d
     */
    public ActorBox2d createActor(ActorType actor, World world, Rectangle shape){
        return switch (actor) {
            case PLAYER -> new Player(world, game.getAssetManager().get("yoshi.jpg"), shape);
            case FLOOR -> new Floor(world, game.getAssetManager().get("floor.png"), shape);
            default -> null;
        };
    }
}
