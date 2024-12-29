package src.world.entities.enemies.dragon;

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
import src.world.entities.enemies.dragon.states.AttackStateDragon;
import src.world.entities.enemies.dragon.states.DamageStateDragon;
import src.world.entities.enemies.dragon.states.IdleStateDragon;
import src.world.entities.enemies.dragon.states.WalkStateDragon;
import src.world.entities.player.powers.PowerUp;

public class DragonEnemy extends Enemy {

    public enum AnimationType {
        IDLE,
        WALK,
        DAMAGE,
        ATTACK
    }

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> damageAnimation;
    private final Animation<TextureRegion> attackAnimation;

    public DragonEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game)
    {
        super(world, shape, assetManager, id, game, Type.DRAGON, PowerUp.Type.NONE, 15);
        sprite.setTexture(assetManager.get("world/entities/dragon/dragonIdle.png", Texture.class));

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

        idleState = new IdleStateDragon(this);
        walkState = new WalkStateDragon(this);
        attackState = new AttackStateDragon(this);
        damageState = new DamageStateDragon(this);
        setState(StateType.IDLE);

        idleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/dragon/dragonIdle.png"), 5));

        walkAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/dragon/dragonWalk.png"), 8));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        damageAnimation = new Animation<>(0.2f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/dragon/dragonDamage.png"), 5));
        damageAnimation.setPlayMode(Animation.PlayMode.LOOP);

        attackAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/dragon/dragonAttack.png"), 4));
    }

    public void setAnimation(DragonEnemy.AnimationType type){
        switch (type){
            case IDLE -> setCurrentAnimation(idleAnimation);
            case WALK -> setCurrentAnimation(walkAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
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
