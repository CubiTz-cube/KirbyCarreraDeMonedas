package src.world.entities.projectiles.enemyProyectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.game.GameScreen;
import src.utils.constants.CollisionFilters;
import src.world.entities.projectiles.SwordProyectil;

public class SwordEnemyProyectil extends SwordProyectil {
    public SwordEnemyProyectil(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, GameScreen game, Texture texture) {
        super(world, shape, assetManager, id, type, game, texture);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.PROJECTIL;
        filter.maskBits = (short)(~CollisionFilters.ITEM & ~CollisionFilters.STATIC & ~CollisionFilters.ENEMY);
        fixture.setFilterData(filter);
    }
}
