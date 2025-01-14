package src.world.entities.player.states;

import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;
import src.world.entities.player.powers.PowerUp;

public class ConsumeState extends StatePlayer{

    public ConsumeState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        if (player.getPowerAbsorded() == PowerUp.Type.NONE) player.setAnimation(Player.AnimationType.CONSUME);
        else player.setAnimation(Player.AnimationType.CONSUMEPOWER);
    }

    @Override
    public void update(Float delta) {
        if (player.isAnimationFinish()) player.consumeEnemy();
    }

    @Override
    public void end() {

    }
}
