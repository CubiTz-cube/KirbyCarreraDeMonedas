package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import src.utils.variables.PlayerControl;
import src.world.entities.player.Player;

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
        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            player.doAction();
        }

        float velocityX = player.getBody().getLinearVelocity().x;

        if (velocityX != 0) player.getBody().setLinearVelocity(velocityX * 0.90f, player.getBody().getLinearVelocity().y);

        if (!Gdx.input.isKeyPressed(PlayerControl.DOWN)) player.setCurrentState(Player.StateType.IDLE);

        if (velocityX > 5 || Gdx.input.isKeyJustPressed(PlayerControl.RIGHT)) {
            player.setFlipX(false);
            player.setCurrentState(Player.StateType.DASH);
        } else if ( velocityX < -5 || Gdx.input.isKeyJustPressed(PlayerControl.LEFT)) {
            player.setFlipX(true);
            player.setCurrentState(Player.StateType.DASH);
        }
    }

    @Override
    public void end() {
    }
}
