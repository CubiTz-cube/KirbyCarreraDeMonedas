package src.world.entities.staticEntity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.world.entities.Entity;

public class StaticEntity extends Entity {
    public StaticEntity(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type) {
        super(world, shape, assetManager, id, type);
    }
}
