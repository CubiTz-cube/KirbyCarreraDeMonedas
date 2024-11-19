package src.world.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.CollisionFilters;
import src.world.ActorBox2d;
import src.world.entities.Entity;

public class Cloud extends Entity {

    public Cloud(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager, id, Type.CLOUD);
        sprite.setTexture(assetManager.get("yoshi.jpg", Texture.class));

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        game.removeEntity(this.getId());
    }
}
