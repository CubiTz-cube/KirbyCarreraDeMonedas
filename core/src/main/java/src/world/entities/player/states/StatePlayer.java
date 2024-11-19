package src.world.entities.player.states;

import src.world.entities.player.Player;
import src.utils.stateMachine.State;

public abstract class StatePlayer implements State {
    protected Player player;

    public StatePlayer (Player player){
        this.player = player;
    }
}
