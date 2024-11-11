package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class FallState extends CanMoveState
{

    public FallState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.FALL);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            player.setState(Player.StateType.ABSORB);
        }

        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.y == 0){
            if (velocity.x == 0)  player.setState(Player.StateType.IDLE);
            else player.setState(Player.StateType.WALK);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.setState(Player.StateType.FLY);
        }
    }

    @Override
    public void end()
    {}
}
