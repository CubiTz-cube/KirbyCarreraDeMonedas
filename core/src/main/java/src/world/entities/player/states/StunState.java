package src.world.entities.player.states;

import src.world.entities.player.Player;

public class StunState extends StatePlayer{
    private Float time = 0f;

    public StunState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        time = 0f;
        player.setAnimation(Player.AnimationType.DAMAGE);
    }

    @Override
    public void update(Float delta) {
        time += delta;
        if (time > player.stunTime) {
            player.setState(Player.StateType.IDLE);
        }
        float velocityX = player.getBody().getLinearVelocity().x;
        if (velocityX != 0) {
            player.getBody().setLinearVelocity(velocityX * 0.70f, player.getBody().getLinearVelocity().y);
        }
    }

    @Override
    public void end() {
        player.stunTime = 2;
    }
}
