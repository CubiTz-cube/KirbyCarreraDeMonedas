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
import src.utils.Box2dUtils;
import src.utils.animation.SheetCutter;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class BombExplosionProyectil extends Projectil{
    public BombExplosionProyectil(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.BOMBEXPLOSION, game, 10);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 6, shape.height / 6);
        fixture = body.createFixture(box, 2);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        //setSpritePosModification(0f, getHeight()/4);

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

    @Override
    public synchronized void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Projectil projectil){
            if (projectil.getType() == Type.BOMBEXPLOSION) return;
            if (projectil.getType() == Type.BOMB) return;
            projectil.despawn();
        }
        if (actor instanceof Enemy enemy){
            if (enemy.getType() == Type.BOMBER) return;
            if (enemy.getCurrentStateType() == Enemy.StateType.DAMAGE) {return;}
            game.actDamageEnemy(enemy.getId(), body, getDamage(), getDamage().floatValue());
        } else if (actor instanceof Player player) {
            if (player.getCurrentStateType() == PlayerCommon.StateType.STUN || player.isInvencible()) {return;}
            player.coinDrop = getDamage();
            player.setCurrentState(Player.StateType.STUN);
            player.playSound(Player.SoundType.NORMALDAMAGE);
            Box2dUtils.knockbackBody(getBody(), body, getDamage());
        }
    }
}
