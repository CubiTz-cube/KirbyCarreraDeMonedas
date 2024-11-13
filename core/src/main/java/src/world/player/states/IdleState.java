package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.variables.Constants;
import src.utils.variables.PlayerControl;
import src.world.player.Player;
import src.utils.stateMachine.StateMachine;

public class IdleState extends CanMoveState{
    public IdleState(Player player){
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.IDLE);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            player.setState(Player.StateType.ABSORB);
        }

        if (Gdx.input.isKeyPressed(PlayerControl.DOWN)){
            player.setState(Player.StateType.DOWN);
        }
        if (Gdx.input.isKeyPressed(PlayerControl.JUMP)){
            player.setState(Player.StateType.JUMP);
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x != 0) player.setState(Player.StateType.WALK);
        if(velocity.y < -1f) player.setState(Player.StateType.FALL);
    }

    @Override
    public void end() {

    }
}
