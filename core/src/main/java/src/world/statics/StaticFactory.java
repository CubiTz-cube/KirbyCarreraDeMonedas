package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.world.ActorBox2d;

public class StaticFactory {

    public enum Type{
        FLOOR,
        SPIKE,
        LAVA,
        //PLATAFORM,
    }

    public ActorBox2d create(Type actor, World world, Rectangle shape){
        return switch (actor) {
            case FLOOR -> new Floor(world, shape);
            case SPIKE -> new Spike(world, shape);
            default -> null;
        };
    }
}
