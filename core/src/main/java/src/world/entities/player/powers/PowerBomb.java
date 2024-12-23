package src.world.entities.player.powers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.world.entities.Entity;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerBomb extends PowerUp{
    private Float cooldown = 0f;

    public PowerBomb(PlayerCommon player) {
        super(player);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void actionIdle() {
        if (cooldown > 0) return;
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.BOMB, 0f,0f);
        cooldown = 1f;
    }

    @Override
    public void actionMove() {
        if (cooldown > 0) return;
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.BOMB, 2f,3f);
        cooldown = 1f;
    }

    @Override
    public void actionAir() {
        if (cooldown > 0) return;
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.BOMB, 1f,8f);
        cooldown = 1f;
    }

    @Override
    public void update(Float delta) {
        if (cooldown > 0){
            cooldown -= delta;
        }
    }

    @Override
    public Animation<TextureRegion> getAnimation(PlayerCommon.AnimationType type) {
        return null;
    }

    @Override
    public Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type) {
        return null;
    }
}
