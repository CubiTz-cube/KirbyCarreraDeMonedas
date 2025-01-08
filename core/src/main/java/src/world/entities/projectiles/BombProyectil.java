package src.world.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;

public class BombProyectil extends Projectil {
    private final Sound explosionSound;

    public BombProyectil(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.BOMB, game, 0);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 4, shape.height / 4);
        fixture = body.createFixture(box, 2);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/4);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.PROJECTIL;
        filter.maskBits = (short)~CollisionFilters.ITEM;
        fixture.setFilterData(filter);

        Animation<TextureRegion> bombAnimation = new Animation<>(0.2f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/bomb.png"), 8));
        setCurrentAnimation(bombAnimation);

        explosionSound = assetManager.get("sound/explosion.wav");
    }

    @Override
    public void act(float delta) {
        if (isAnimationFinish()) {
            game.addEntityNoPacket(Type.BOMBEXPLOSION, new Vector2(body.getPosition()).add(-5.5f, -5.5f),new Vector2(0,0), false);
            game.playProximitySound(explosionSound, body.getPosition(), 40f);
            game.addCameraShakeProximity(body.getPosition(), 40f, 0.5f, 10);
            despawn();
        }
    }

    @Override
    public synchronized void beginContactWith(ActorBox2d actor, GameScreen game) {

    }
}
