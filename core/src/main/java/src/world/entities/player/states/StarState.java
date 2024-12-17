package src.world.entities.player.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.Entity;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class StarState extends StatePlayer{
    public StarState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.FLYEND);
        player.playSound(Player.SoundType.STAR);
        player.game.addCameraShake(0.2f, 5f);
        player.throwEntity(Entity.Type.STAR, 3f);
        player.enemyAbsorded = null;
    }

    @Override
    public void update(Float delta) {
        if (player.isAnimationFinish()) player.setCurrentState(PlayerCommon.StateType.IDLE);
    }

    @Override
    public void end() {

    }
}
