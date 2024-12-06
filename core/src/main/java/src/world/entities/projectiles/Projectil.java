package src.world.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class Projectil extends Entity {
    protected GameScreen game;
    private Boolean despawn;

    public Projectil(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type, GameScreen game) {
        super(world, shape, assetManager, id, type);
        this.game = game;
        despawn = false;
    }

    @Override
    public synchronized void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Player) return;
        despawn();
    }

    public synchronized void despawn(){
        if (despawn) return;
        game.removeEntityNoPacket(this.getId());
        despawn = true;
    }
}
