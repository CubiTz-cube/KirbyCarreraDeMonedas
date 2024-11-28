package src.world.entities.blocks.states;

import src.utils.stateMachine.State;
import src.world.entities.blocks.Block;

public abstract class StateBlock implements State {
    protected final Block block;

    public StateBlock(Block block) {
        this.block = block;
    }
}
