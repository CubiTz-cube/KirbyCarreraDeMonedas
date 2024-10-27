package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.ActorBox2d;

public class StaticFactory {
    private final Main game;
    public enum Type{
        FLOOR
    }

    public StaticFactory(Main game){
        this.game = game;
    }

    public ActorBox2d create(Type actor, World world, Rectangle shape){
        return switch (actor) {
            case FLOOR -> new Floor(world, game.getAssetManager().get("floor.png"), shape);
            default -> null;
        };
    }
}
