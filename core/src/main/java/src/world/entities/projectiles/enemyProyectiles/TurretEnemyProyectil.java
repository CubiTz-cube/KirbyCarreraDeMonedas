package src.world.entities.projectiles.enemyProyectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.game.GameScreen;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.projectiles.Projectil;

public class TurretEnemyProyectil extends Projectil {

    private Float time;

    public TurretEnemyProyectil(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, GameScreen game, Texture texture)
    {
        super(world, shape, assetManager, id, Type.BOMB, game, 3);
        sprite.setTexture(assetManager.get("world/particles/turretParticle.png"));
        time = 0f;

        BodyDef def = new BodyDef();
        def.position.set(shape.x + shape.width / 2, shape.y + shape.height / 2);
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
        filter.maskBits = (short)(~CollisionFilters.ITEM & ~CollisionFilters.ENEMY);
        fixture.setFilterData(filter);

    }

    @Override
    public void act(float delta) {
        time += delta;
        if (time > 3f) {
            despawn();
        }
    }

    @Override
    public synchronized void beginContactWith(ActorBox2d actor, GameScreen game) {
        super.beginContactWith(actor, game);
        if (actor instanceof Enemy enemy) {
            if (enemy.getCurrentStateType() != Enemy.StateType.DAMAGE) {
                enemy.takeDamage(1);
                despawn();
            }
        }
    }
}
