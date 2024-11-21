package src.world.entities.enemies.sleeping;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.screens.GameScreen;
import src.utils.CollisionFilters;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.sleeping.states.*;
import src.world.entities.player.powers.PowerUp;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class SleepingEnemy extends Enemy
{

    public SleepingEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game)
    {
        super(world, shape, assetManager,id, game, Type.SLEEPY, PowerUp.Type.SLEEP, 3);
        sprite.setTexture(assetManager.get("yozhi.jpg", Texture.class));

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

        Filter filter = new Filter();
        filter.categoryBits = ~CollisionFilters.CATEGORY_ENEMY;
        filter.maskBits = CollisionFilters.MASK_ENEMY;
        fixture.setFilterData(filter);

        idleState = new IdleStateSleepy(this);
        walkState = new WalkStateSleepy(this);
        damageState = new DamageStateSleepy(this);
        setState(StateType.IDLE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        setPosition(
            body.getPosition().x * PIXELS_IN_METER,
            body.getPosition().y * PIXELS_IN_METER
        );
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
        sprite.setOriginCenter();
        sprite.draw(batch);
    }
}
