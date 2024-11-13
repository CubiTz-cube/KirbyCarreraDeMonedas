package src.world.entities.breakBlocks.states;

import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.entities.breakBlocks.BreakBlock;

public abstract class StateBreaklBlock implements State {
    protected final BreakBlock block;

    public StateBreaklBlock(BreakBlock block) {
        this.block = block;
    }
}
