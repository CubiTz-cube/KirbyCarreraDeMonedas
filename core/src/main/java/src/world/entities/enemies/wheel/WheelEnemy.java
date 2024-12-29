package src.world.entities.enemies.wheel;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
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
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.wheel.states.DamageStateWheel;
import src.world.entities.enemies.wheel.states.IdleStateWheel;
import src.world.entities.enemies.wheel.states.WalkStateWheel;
import src.world.entities.player.powers.PowerUp;

public class WheelEnemy extends Enemy
{
    public enum AnimationType {
        IDLE,
        WALK,
        DAMAGE
    }


    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> damageAnimation;

    public WheelEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game)
    {
        super(world, shape, assetManager, id, game, Type.WHEEL, PowerUp.Type.WHEEL, 15);
        sprite.setTexture(assetManager.get("world/entities/wheel/wheelIdle.png", Texture.class));

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
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

        idleState = new IdleStateWheel(this);
        walkState = new WalkStateWheel(this);
        damageState = new DamageStateWheel(this);

        idleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/wheel/wheelIdle.png"), 2));

        walkAnimation = new Animation<>(0.2f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/wheel/wheelWalk.png"), 3));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        damageAnimation = new Animation<>(0.2f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/wheel/wheelDamage.png"), 4));
        damageAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setState(StateType.IDLE);
    }
    public void setAnimation(WheelEnemy.AnimationType type)
    {
        switch (type) {
            case IDLE -> setCurrentAnimation(idleAnimation);
            case WALK -> setCurrentAnimation(walkAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
        }
    }
}
