package src.world.entities.enemies.fly;

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
import src.world.ActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.fly.states.DamageStateFly;
import src.world.entities.enemies.fly.states.WalkStateFly;
import src.world.entities.enemies.fly.states.IdleStateFly;
import src.world.entities.player.powers.PowerUp;
import src.world.entities.projectiles.Projectil;

public class FlyEnemy extends Enemy
{
    public Boolean flyDown;
    public enum AnimationType {
        IDLE,
        WALK,
        DAMAGE
    }

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> damageAnimation;

    public FlyEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager,id, game, Type.FLYBUG, PowerUp.Type.NONE,9);
        flyDown = false;

        BodyDef def = new BodyDef();
        def.position.set(shape.x + shape.width / 2, shape.y + shape.height / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/4);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.ENEMY;
        filter.maskBits = (short)~CollisionFilters.ENEMY;
        fixture.setFilterData(filter);

        idleState = new IdleStateFly(this);
        walkState = new WalkStateFly(this);
        damageState = new DamageStateFly(this);

        setState(StateType.WALK);

        idleAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/fly/flyIdle.png"), 4));

        walkAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/fly/flyIdle.png"), 4));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        damageAnimation = new Animation<>(0.2f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/fly/flyDamage.png"), 4));
        damageAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setCurrentAnimation(walkAnimation);
    }

    public void setAnimation(AnimationType type) {
        switch (type) {
            case IDLE -> setCurrentAnimation(idleAnimation);
            case WALK -> setCurrentAnimation(walkAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
        }
    }

    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        super.beginContactWith(actor, game);
        if (actor instanceof Projectil) return;
        if (getCurrentStateType() == StateType.DAMAGE) return;
        setState(Enemy.StateType.IDLE);
    }
}
