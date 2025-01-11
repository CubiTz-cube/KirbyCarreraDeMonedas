package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.screens.GameScreen;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;
import src.world.statics.MovingPlatfromLimiter;

public class MovingPlatform extends Entity implements NoAutoPacketEntity {

    private float dirX;
    private float dirY;

    public MovingPlatform(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, float dirX, float dirY) {
        super(world, shape, assetManager, id, type);
        this.dirX = dirX;
        this.dirY = dirY;
        sprite.setTexture(assetManager.get("world/entities/movingPlatform.png", Texture.class));

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(shape.x + shape.width / 2, shape.y + shape.height / 2);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2 );
        fixture = body.createFixture(box, 1f);
        fixture.setUserData(this);
        box.dispose();

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.MVINGPLAT;
        fixture.setFilterData(filter);
    }

    @Override
    public void act(float delta) {
        body.setLinearVelocity(dirX, dirY);
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof MovingPlatfromLimiter) {
            dirX = -dirX;
            dirY = -dirY;
        }
    }
}
