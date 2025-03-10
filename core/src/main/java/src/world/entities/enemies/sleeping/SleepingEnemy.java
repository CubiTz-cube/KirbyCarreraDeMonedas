package src.world.entities.enemies.sleeping;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.screens.game.GameScreen;
import src.utils.animation.SheetCutter;
import src.utils.constants.CollisionFilters;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.sleeping.states.*;
import src.world.entities.player.powers.PowerUp;

public class SleepingEnemy extends Enemy
{
    public enum AnimationType {
        IDLE,
        DAMAGE,
    }

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> damageAnimation;

    public SleepingEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game)
    {
        super(world, shape, assetManager,id, game, Type.SLEEPY, PowerUp.Type.SLEEP, 9);

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

        idleState = new IdleStateSleepy(this);
        damageState = new DamageStateSleepy(this);
        setState(StateType.IDLE);

        idleAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/sleepy/sleepyIdle.png"), 6));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        damageAnimation = new Animation<>(0.2f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/sleepy/sleepyDamage.png"), 6));
        damageAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setAnimation(AnimationType.IDLE);
    }
    public void setAnimation(SleepingEnemy.AnimationType type){
        switch (type){
            case IDLE -> setCurrentAnimation(idleAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
        }
    }
}
