package src.entities.player.states;

import src.entities.player.Player;
import src.utils.stateMachine.StateMachine;

public class JumpState extends StatePlayer{
    public JumpState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.jump();
    }

    @Override
    public void update() {

    }

    @Override
    public void end() {

    }
}
