package src.world.player.states;

import src.world.player.Player;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;

public abstract class StatePlayer implements State {
    protected Player player;

    public StatePlayer (Player player){
        this.player = player;
    }
}
