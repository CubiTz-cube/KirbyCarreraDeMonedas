package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import src.utils.variables.PlayerControl;
import src.world.entities.player.Player;

public abstract class CanBasicMoveState extends CanMoveState{

    public CanBasicMoveState(Player player) {
        super(player);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION) && player.enemyAbsorded == null){
            player.setState(Player.StateType.ABSORB);
        }
        if (Gdx.input.isKeyPressed(PlayerControl.JUMP) && player.getBody().getLinearVelocity().y == 0){
            player.setState(Player.StateType.JUMP);
        }
        if (Gdx.input.isKeyPressed(PlayerControl.DOWN)){
            if (player.enemyAbsorded == null) player.setState(Player.StateType.DOWN);
            else player.setState(Player.StateType.CONSUME);
        }
    }
}
