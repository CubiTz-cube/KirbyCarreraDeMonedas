package src.world.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;

public class SwordRunProyectil extends Projectil{
    private Float timeDespawn;

    public SwordRunProyectil(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, GameScreen game) {
        super(world, shape, assetManager, id, type, game, 3);
        timeDespawn = 0f;
        sprite.setTexture(assetManager.get("world/particles/kirbySwordParticle.png"));

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 2);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.PROJECTIL;
        filter.maskBits = (short)(~CollisionFilters.ITEM & ~CollisionFilters.STATIC);
        fixture.setFilterData(filter);
    }

    @Override
    public synchronized void beginContactWith(ActorBox2d actor, GameScreen game) {
        super.beginContactWith(actor, game);
        despawn();
    }

    @Override
    public void act(float delta) {
        timeDespawn += delta;
        if (timeDespawn > 1f) {
            despawn();
        }
    }
}
