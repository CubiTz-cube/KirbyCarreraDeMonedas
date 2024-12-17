package src.world.entities.player.states;

import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class StunState extends StatePlayer{
    private Float time = 0f;

    public StunState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        time = 0f;
        player.setAnimation(Player.AnimationType.DAMAGE);
        player.game.addCameraShake(0.2f, 10f);
    }

    @Override
    public void update(Float delta) {
        time += delta;
        if (time > player.stunTime) {
            player.setCurrentState(Player.StateType.IDLE);
        }
        float velocityX = player.getBody().getLinearVelocity().x;
        if (velocityX != 0) {
            player.getBody().setLinearVelocity(velocityX * 0.70f, player.getBody().getLinearVelocity().y);
        }
    }

    @Override
    public void end() {
        player.stunTime = Player.DEFAULT_STUNT_TIME;
        player.setInvencible(2f);
        player.lossPoints(player.coinDrop);
        player.setCurrentPowerUp(null);
        player.coinDrop = Player.DEFAULT_COIN_DROP;
    }
}
