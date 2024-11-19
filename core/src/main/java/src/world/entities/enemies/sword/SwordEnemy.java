package src.world.entities.enemies.sword;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.sword.states.AttackStateSword;
import src.world.entities.enemies.sword.states.IdleStateSword;
import src.world.entities.enemies.sword.states.WalkStateSword;
import src.world.player.powers.PowerUp;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class SwordEnemy extends Enemy
{
    public SwordEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id)
    {
        super(world, shape, assetManager, id);
        type = Type.SWORD;
        powerUp = PowerUp.Type.SWORD;
        sprite.setTexture(assetManager.get("yoshiSword.png", Texture.class));
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

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

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);

        idleState = new IdleStateSword(this);
        walkState = new WalkStateSword(this);
        attackState = new AttackStateSword(this);

        setState(StateType.IDLE);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void detach()
    {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
