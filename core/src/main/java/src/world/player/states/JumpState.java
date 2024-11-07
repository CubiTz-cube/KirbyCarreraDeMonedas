package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import src.world.player.Player;
import src.utils.stateMachine.StateMachine;

public class JumpState extends CanMoveState{
    private Float jumpTime = 0f;

    public JumpState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.JUMP);
        jumpTime = 0f;
        player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            player.setState(Player.StateType.ABSORB);
        }

        if (jumpTime < Player.MAX_JUMP_TIME && Gdx.input.isKeyPressed(Input.Keys.UP)){
            jumpTime += delta;
            player.getBody().applyLinearImpulse(0, Player.JUMP_INAIR, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.setState(Player.StateType.FLY);
        }
        if (player.getBody().getLinearVelocity().y < 0){
            player.setState(Player.StateType.FALL);
        }
    }

    @Override
    public void end() {

    }
}
