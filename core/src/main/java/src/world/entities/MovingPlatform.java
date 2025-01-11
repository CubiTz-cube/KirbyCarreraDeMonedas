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

    private final Filter filter;
    private float dirx;
    private float diry;

    public MovingPlatform(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, float dirx, float diry) {
        super(world, shape, assetManager, id, type);
        this.dirx = dirx;
        this.diry = diry;
        sprite.setTexture(assetManager.get("world/entities/movingPlatform.png", Texture.class));

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2 );
        Fixture fixture = body.createFixture(box, 1f);
        box.dispose();

        filter = new Filter();
        filter.categoryBits = CollisionFilters.MVINGPLAT;
        filter.maskBits = (short) (CollisionFilters.PLAYER | CollisionFilters.STATIC | CollisionFilters.ENEMY | CollisionFilters.OTHERPLAYER);
        fixture.setFilterData(filter);


        fixture.setUserData(this);


        setSize(shape.width,shape.height);
        setPosition(shape.x, shape.y);
    }

    @Override
    public void act(float delta) {
        body.setLinearVelocity(dirx, diry);
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof MovingPlatfromLimiter) {
            dirx = -dirx;
            diry = -diry;
        }
    }
}
