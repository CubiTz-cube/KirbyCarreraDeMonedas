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
        player.getSprite().setColor(Color.GOLDENROD);
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
            stateMachine.setState(player.getIdleState());
        }
    }

    @Override
    public void end() {

    }
}
