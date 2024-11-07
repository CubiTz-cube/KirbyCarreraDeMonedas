package src.world.player.states;

import com.badlogic.gdx.graphics.Color;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class DashState extends StatePlayer{
    public DashState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.DASH);
        player.getBody().applyLinearImpulse(
            player.getSprite().isFlipX() ? -Player.DASH_IMPULSE : Player.DASH_IMPULSE,
            0, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        float velocityX = player.getBody().getLinearVelocity().x;
        if (velocityX != 0) {
            player.getBody().setLinearVelocity(velocityX * 0.90f, player.getBody().getLinearVelocity().y);
        }else {
            player.setState(Player.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
