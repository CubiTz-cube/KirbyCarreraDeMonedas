package src.world.entities.items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.constants.CollisionFilters;
import src.world.entities.player.powers.PowerUp;

public class PowerItem extends Item {
    private final PowerUp.Type powerType;

    public PowerItem(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game, PowerUp.Type powerType) {
        super(world, shape, assetManager, id, Type.COIN, game);
        this.powerType = powerType;
        //sprite.setTexture(assetManager.get("logo.png", Texture.class));

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
        filter.categoryBits = CollisionFilters.ITEM;
        filter.maskBits = (short)~(CollisionFilters.ENEMY | CollisionFilters.ITEM);
        fixture.setFilterData(filter);
    }

    public PowerUp.Type getPowerType() {
        return powerType;
    }
}
