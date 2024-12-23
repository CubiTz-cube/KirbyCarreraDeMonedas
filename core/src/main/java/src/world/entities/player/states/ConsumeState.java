package src.world.entities.player.states;

import src.world.entities.player.Player;

public class ConsumeState extends StatePlayer{

    public ConsumeState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.CONSUME);
    }

    @Override
    public void update(Float delta) {
        if (player.isAnimationFinish()) player.consumeEnemy();
    }

    @Override
    public void end() {

    }
}
