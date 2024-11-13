package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import src.utils.stateMachine.StateMachine;
import src.utils.variables.PlayerControl;
import src.world.player.Player;

public class DownState  extends StatePlayer{
    public DownState(Player player)
    {
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.DOWN);
    }

    @Override
    public void update(Float delta) {
        float velocityX = player.getBody().getLinearVelocity().x;

        if (velocityX != 0) player.getBody().setLinearVelocity(velocityX * 0.90f, player.getBody().getLinearVelocity().y);

        if (!Gdx.input.isKeyPressed(PlayerControl.DOWN)) player.setState(Player.StateType.IDLE);

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            player.setState(Player.StateType.ABSORB);
        }

        if (Gdx.input.isKeyJustPressed(PlayerControl.RIGHT)) {
            player.setFlipX(false);
            player.setState(Player.StateType.DASH);
        } else if ( Gdx.input.isKeyJustPressed(PlayerControl.LEFT)) {
            player.setFlipX(true);
            player.setState(Player.StateType.DASH);
        }
    }

    @Override
    public void end() {
    }
}
