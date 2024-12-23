package src.world.entities.player.powers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerBomb extends PowerUp{
    public PowerBomb(PlayerCommon player) {
        super(player);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void actionIdle() {

    }

    @Override
    public void actionMove() {

    }

    @Override
    public void actionAir() {

    }

    @Override
    public void update(Float delta) {

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
