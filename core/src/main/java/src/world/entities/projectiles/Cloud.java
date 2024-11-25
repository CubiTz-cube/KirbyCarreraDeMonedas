package src.world.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;
import src.world.ActorBox2d;
import src.world.entities.Entity;

public class Cloud extends Entity {
    private GameScreen game;
    private Float timeDespawn;
    private Boolean despawn;

    public Cloud(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.CLOUD);
        this.game = game;
        timeDespawn = 0f;
        despawn = false;

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        Animation<TextureRegion> cloudAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/particles/cloudParticle.png"), 5));
        setCurrentAnimation(cloudAnimation);
    }

    @Override
    public void act(float delta) {
        timeDespawn += delta;
        if (timeDespawn > 0.3f) {
            game.removeEntityNoPacket(this.getId());
        }
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (!despawn) game.removeEntityNoPacket(this.getId()); despawn = true;
    }
}
