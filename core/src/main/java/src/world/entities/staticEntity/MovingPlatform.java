package src.world.entities.staticEntity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static src.utils.variables.Constants.PIXELS_IN_METER;

public class MovingPlatform extends StaticEntity {

    public MovingPlatform(World world, Rectangle shape, AssetManager assetManager, Integer id,Type type, Vector2 impulso) {
        super(world, shape, assetManager,id, type);
        sprite.setTexture(assetManager.get("world/entities/breakBlock.png", Texture.class));

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(shape.x, shape.y);
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.x / 2, shape.y / 2);

        fixture = body.createFixture(box, 0.0f);
        fixture.setUserData(this);
        box.dispose();

        body.setLinearVelocity(impulso);
        setPosition(shape.x * PIXELS_IN_METER, shape.y * PIXELS_IN_METER);
    }
}
