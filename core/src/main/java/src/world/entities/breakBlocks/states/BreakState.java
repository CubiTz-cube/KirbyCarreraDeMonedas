package src.world.entities.breakBlocks.states;

import src.utils.stateMachine.StateMachine;
import src.world.entities.breakBlocks.BreakBlock;

public class BreakState extends StateBreaklBlock{
    private Float time = 0f;

    public BreakState(BreakBlock block) {
        super(block);
    }

    @Override
    public void start() {
        block.setAnimation(BreakBlock.AnimationType.BREAK);
        block.setColision(false);
    }

    @Override
    public void update(Float delta) {
        time += delta;
        if (time >= 3) {
            block.setState(BreakBlock.StateType.LIVE);
        }
    }

    @Override
    public void end() {
        time = 0f;
    }
}
