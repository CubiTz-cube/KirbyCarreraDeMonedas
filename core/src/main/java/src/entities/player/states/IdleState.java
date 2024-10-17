package src.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import src.entities.player.Player;
import src.utils.stateMachine.StateMachine;

public class IdleState extends StatePlayer{
    public IdleState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            stateMachine.setState(player.getJumpState());
        }
    }

    @Override
    public void end() {

    }
}
