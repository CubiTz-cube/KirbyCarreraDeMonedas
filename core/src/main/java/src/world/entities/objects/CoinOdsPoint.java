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
    protected Game game;
    //private final Fixture sensorFixture;

    public CoinOdsPoint(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager, id, Type.COIN);
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
        filter.categoryBits = CollisionFilters.CATEGORY_COIN;
        filter.maskBits = CollisionFilters.MASK_ENEMY;
        fixture.setFilterData(filter);

        /*PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(shape.width / 4, shape.height / 4);

        FixtureDef sensorFixtureDef = new FixtureDef();
        sensorFixtureDef.shape = sensorShape;
        sensorFixtureDef.isSensor = true;

        sensorFixture = body.createFixture(sensorFixtureDef);
        sensorFixture.setUserData(this);
        sensorShape.dispose();*/
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        super.beginContactWith(actor, game);
        if(actor instanceof Lava) {
            game.removeEntity(getId());
        }
    }

    @Override
    public void detach() {
        super.detach();
        //body.destroyFixture(sensorFixture);
    }
}
