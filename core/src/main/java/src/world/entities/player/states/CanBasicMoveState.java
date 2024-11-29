package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.variables.PlayerControl;
import src.world.entities.player.Player;

public abstract class CanBasicMoveState extends CanMoveState{

    public CanBasicMoveState(Player player) {
        super(player);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        Vector2 velocity = player.getBody().getLinearVelocity();

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            player.doAction();
        }
        if (Gdx.input.isKeyPressed(PlayerControl.JUMP) && player.getBody().getLinearVelocity().y == 0){
            player.setCurrentState(Player.StateType.JUMP);
        }
        if (Gdx.input.isKeyPressed(PlayerControl.DOWN)){
            if (player.enemyAbsorded == null) player.setCurrentState(Player.StateType.DOWN);
            else player.setCurrentState(Player.StateType.CONSUME);
        }
        if (velocity.y < -1){
            player.setCurrentState(Player.StateType.FALL);
        }
    }
}
