package src.world.entities.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.world.ActorBox2d;
import src.world.entities.projectiles.Projectil;

public class BreakBlock extends Block {

    public BreakBlock(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager,id, Type.BREAKBLOCK, game);
        sprite.setTexture((assetManager.get("world/entities/breakBlock.png")));
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Projectil proyectil) {
            if (proyectil.getDamage() == 0 || getCurrentStateType() == StateType.BREAK) return;
            setState(StateType.BREAK);
            proyectil.despawn();
        }
    }
}
