package src.world.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;
import src.utils.constants.CollisionFilters;

public class BombExplosionProyectile extends Projectil{
    public BombExplosionProyectile(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.BOMBEXPLOSION, game, 10);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 4, shape.height / 4);
        fixture = body.createFixture(box, 2);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/16);

        Filter filter = new Filter();
        filter.maskBits = (short)(~CollisionFilters.ITEM & ~CollisionFilters.STATIC);
        fixture.setFilterData(filter);

        Animation<TextureRegion> cloudAnimation = new Animation<>(0.05f,
            SheetCutter.cutHorizontal(assetManager.get("world/particles/bombParticle.png"), 5));
        setCurrentAnimation(cloudAnimation);
    }

    @Override
    public void act(float delta) {
        if (isAnimationFinish()) despawn();
    }
}
