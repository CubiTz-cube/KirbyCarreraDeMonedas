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

public class Cloud extends Projectil {
    private Float timeDespawn;

    public Cloud(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.CLOUD, game);
        timeDespawn = 0f;

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 3, shape.height / 3);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/3);

        Animation<TextureRegion> cloudAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/particles/cloudParticle.png"), 5));
        setCurrentAnimation(cloudAnimation);
    }

    @Override
    public void act(float delta) {
        timeDespawn += delta;
        if (timeDespawn > 0.5f) {
            game.removeEntityNoPacket(this.getId());
        }
    }
}
