package src.world.entities.enemies.sword;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;
import src.utils.constants.CollisionFilters;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sword.states.AttackStateSword;
import src.world.entities.enemies.sword.states.DamageStateSword;
import src.world.entities.enemies.sword.states.IdleStateSword;
import src.world.entities.enemies.sword.states.WalkStateSword;
import src.world.entities.player.powers.PowerUp;

public class SwordEnemy extends Enemy
{
    public enum AnimationType {
        IDLE,
        WALK,
        DAMAGE,
        ATTACK
    }

    /*private final Animation<TextureRegion> idleAnimation;*/
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> damageAnimation;
    private final Animation<TextureRegion> attackAnimation;

    public SwordEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game)
    {
        super(world, shape, assetManager, id, game, Type.SWORD, PowerUp.Type.SWORD, 15);
        sprite.setTexture(assetManager.get("world/entities/sword/swordEnemyIdle.png", Texture.class));

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

        idleState = new IdleStateSword(this);
        walkState = new WalkStateSword(this);
        attackState = new AttackStateSword(this);
        damageState = new DamageStateSword(this);
        setState(StateType.IDLE);

        walkAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/sword/swordEnemyWalk.png"), 8));
        damageAnimation = new Animation<>(0.01f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/sword/swordEnemyDamage.png"), 5));
        attackAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/sword/swordEnemyAttack.png"), 6));
    }

    public void setAnimation(SwordEnemy.AnimationType type){
        switch (type){
            /*case IDLE -> setCurrentAnimation(idleAnimation);*/
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
