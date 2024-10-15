package src.entities.player.states;

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

    }

    @Override
    public void end() {

    }
}
