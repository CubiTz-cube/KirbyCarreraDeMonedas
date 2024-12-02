package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.world.ActorBox2d;
da
public class StaticFactory {

    public enum Type{
        FLOOR,
        SPIKE,
        LAVA,
        MOVINGPLATFORMLIMITER,
        //PLATAFORM,
    }

    public ActorBox2d create(Type actor, World world, Rectangle shape){
        return switch (actor) {
            case FLOOR -> new Floor(world, shape);
            case SPIKE -> new Spike(world, shape);
            case LAVA -> new Lava(world, shape);
            case MOVINGPLATFORMLIMITER -> new MovingPlatfromLimiter(world, shape);
            default -> null;
        };
    }
}
