package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;

import static src.utils.constants.Constants.PIXELS_IN_METER;

public class FloorPoly  extends ActorBox2d {
    public FloorPoly(World world, Rectangle shape, Vector2[] vertices){
        super(world, shape);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-2) / 2, shape.y + (shape.height-2)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape poly = new PolygonShape();
        poly.set(vertices);
        fixture = body.createFixture(poly, 1);
        fixture.setUserData(this);
        poly.dispose();
        fixture.setFriction(1.5f);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.STATIC;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setPosition(shape.x * PIXELS_IN_METER, shape.y * PIXELS_IN_METER);
    }
}
