package src.world.entities.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.screens.GameScreen;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.statics.Lava;

public class CoinOdsPoint extends Entity {
    protected GameScreen game;

    public CoinOdsPoint(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.COIN);
        this.game = game;
        sprite.setTexture(assetManager.get("world/entities/coin.png", Texture.class));

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/2);
        fixture = body.createFixture(box, 0.5f);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.COIN;
        filter.maskBits = (short)~(CollisionFilters.ENEMY | CollisionFilters.COIN);
        fixture.setFilterData(filter);
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        super.beginContactWith(actor, game);
        if(actor instanceof Lava) {
            despawn();
        }
    }

    public synchronized void despawn(){
        game.removeEntity(getId());
    }
}
