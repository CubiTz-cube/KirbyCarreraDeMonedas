package src.world.entities.enemies.turret;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;
import src.utils.constants.CollisionFilters;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.turret.states.AttackStateTurret;
import src.world.entities.enemies.turret.states.IdleStateTurret;
import src.world.entities.player.powers.PowerUp;

public class TurretEnemy extends Enemy
{
    public enum AnimationType {
        IDLE,
        ATTACK
    }

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> attackAnimation;

    public TurretEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game)
    {
        super(world, shape, assetManager, id, game, Type.TURRET, PowerUp.Type.NONE, 15);
        sprite.setTexture(assetManager.get("world/entities/turret/turretEnemy.png", Texture.class));

        BodyDef def = new BodyDef();
        def.position.set(shape.x + shape.width / 2, shape.y + shape.height / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 4, shape.height / 4);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/4);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.ENEMY;
        filter.maskBits = (short)~CollisionFilters.ENEMY;
        fixture.setFilterData(filter);

        idleState = new IdleStateTurret(this);
        attackState = new AttackStateTurret(this);
        setState(StateType.IDLE);

        idleAnimation = new Animation<>(1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/turret/turretEnemy.png"), 2));

        attackAnimation = new Animation<>(1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/turret/turretEnemy.png"), 2));
    }

    public void setAnimation(TurretEnemy.AnimationType type){
        switch (type){
            case IDLE -> setCurrentAnimation(idleAnimation);
            case ATTACK -> setCurrentAnimation(attackAnimation);
        }
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
}
