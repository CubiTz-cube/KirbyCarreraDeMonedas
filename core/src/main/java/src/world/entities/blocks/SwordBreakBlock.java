package src.world.entities.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.game.GameScreen;
import src.world.ActorBox2d;
import src.world.entities.projectiles.Projectil;
import src.world.entities.projectiles.SwordProyectil;
import src.world.entities.projectiles.SwordRunProyectil;

public class SwordBreakBlock extends Block{

    public SwordBreakBlock(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, GameScreen game) {
        super(world, shape, assetManager, id, type, game);
        sprite.setTexture((assetManager.get("world/entities/blocks/swordBreakBlock.png")));
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof SwordProyectil || actor instanceof SwordRunProyectil ) {
            Projectil proyectil = (Projectil) actor;
            if (proyectil.getDamage() == 0 || getCurrentStateType() == StateType.BREAK) return;
            setState(StateType.BREAK);
            proyectil.despawn();
        }
    }
}
