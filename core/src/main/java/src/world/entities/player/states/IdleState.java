package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import src.world.entities.player.Player;
import src.utils.stateMachine.StateMachine;

public class IdleState extends StatePlayer{
    public IdleState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(Float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            stateMachine.setState(player.getJumpState());
        }
    }

    @Override
    public void end() {

    }
}
