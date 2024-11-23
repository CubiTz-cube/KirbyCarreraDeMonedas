package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import src.main.Main;
import src.world.ActorBox2d;

public class StaticFactory {
    public enum Type{
        FLOOR
    }

    public ActorBox2d create(Type actor, World world, Rectangle shape){
        return switch (actor) {
            case FLOOR -> new Floor(world, shape);
            default -> null;
        };
    }
}
