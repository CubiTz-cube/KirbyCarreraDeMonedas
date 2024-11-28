package src.world.entities.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class BreakBlock extends Block {

    public BreakBlock(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager,id, Type.BREAKBLOCK);
        sprite.setTexture((assetManager.get("world/entities/breakBlock.png")));
    }

}
