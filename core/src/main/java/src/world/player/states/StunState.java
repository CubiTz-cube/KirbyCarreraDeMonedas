package src.world.player.states;

import com.badlogic.gdx.graphics.Color;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class StunState extends StatePlayer{
    private Float time = 0f;

    public StunState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        time = 0f;
        player.setAnimation(Player.AnimationType.DAMAGE);
    }

    @Override
    public void update(Float delta) {
        time += delta;
        if (time > 2f) {
            player.setState(Player.StateType.IDLE);
        }
        float velocityX = player.getBody().getLinearVelocity().x;
        if (velocityX != 0) {
            player.getBody().setLinearVelocity(velocityX * 0.70f, player.getBody().getLinearVelocity().y);
        }
    }

    @Override
    public void end() {
        player.getSprite().setColor(Color.WHITE);
    }
}
