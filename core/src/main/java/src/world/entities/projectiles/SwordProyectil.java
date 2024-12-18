package src.world.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;

public class SwordProyectil extends Projectil{
    private Float timeDespawn;

    public SwordProyectil(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, GameScreen game, Texture texture) {
        super(world, shape, assetManager, id, type, game, 5);
        timeDespawn = 0f;
        sprite.setTexture(texture);

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
    }

    @Override
    public void act(float delta) {
        timeDespawn += delta;
        if (timeDespawn > 0.5f) {
            despawn();
        }
    }
}
