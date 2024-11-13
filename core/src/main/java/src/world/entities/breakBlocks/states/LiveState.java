package src.world.entities.breakBlocks.states;

import src.utils.stateMachine.StateMachine;
import src.world.entities.breakBlocks.BreakBlock;

public class LiveState extends StateBreaklBlock{
    public LiveState(BreakBlock block) {
        super(block);
    }

    @Override
    public void start() {
        block.setAnimation(BreakBlock.AnimationType.LIVE);
        block.setColision(true);
    }

    @Override
    public void update(Float delta) {
    }

    @Override
    public void end() {

    }
}
