package src.world.entities.blocks.states;

import src.world.entities.blocks.Block;

public class LiveState extends StateBlock{
    public LiveState(Block block) {
        super(block);
    }

    @Override
    public void start() {
        block.setAnimation(Block.AnimationType.LIVE);
        block.setColision(true);
    }

    @Override
    public void update(Float delta) {
    }

    @Override
    public void end() {

    }
}
