package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static src.utils.constants.Constants.PIXELS_IN_METER;

public class MovingPlatfromLimiter extends Floor {

    public MovingPlatfromLimiter(World world, Rectangle shape) {
        super(world, shape);
    }

}
