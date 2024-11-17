package src.world.entities.mirror;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.animation.SheetCutter;
import src.world.entities.Entity;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class Mirror extends Entity {

    public Mirror(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager,id);
        type = Type.MIRROR;
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/2, shape.height/2);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData(this);
        box.dispose();

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);

        Animation<TextureRegion> loopAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/mirror/mirrorLoop.png"), 4));
        loopAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setCurrentAnimation(loopAnimation);
    }

    @Override
    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
