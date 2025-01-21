package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;

public class MovingPlatfromLimiter extends ActorBox2d {

    private final Filter filter;

    public MovingPlatfromLimiter(World world, Rectangle shape) {
        super(world, shape);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + shape.width / 2, shape.y + shape.height/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1f);
        fixture.setUserData(this);
        fixture.setSensor(true);
        box.dispose();

        filter = new Filter();
        filter.categoryBits = CollisionFilters.STATIC;
        filter.maskBits = CollisionFilters.MVINGPLAT;
        fixture.setFilterData(filter);
    }
}
