package src.world.entities.staticEntity.breakBlocks.states;

import src.utils.stateMachine.State;
import src.world.entities.staticEntity.breakBlocks.BreakBlock;

public abstract class StateBreaklBlock implements State {
    protected final BreakBlock block;

    public StateBreaklBlock(BreakBlock block) {
        this.block = block;
    }
}
