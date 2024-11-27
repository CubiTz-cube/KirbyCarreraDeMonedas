package src.world.entities.staticEntity.blocks.states;

import src.world.entities.staticEntity.blocks.Block;

public class BreakState extends StateBlock{
    private Float time = 0f;

    public BreakState(Block block) {
        super(block);
    }

    @Override
    public void start() {
        block.setAnimation(Block.AnimationType.BREAK);
        block.setColision(false);
    }

    @Override
    public void update(Float delta) {
        time += delta;
        if (time >= 3) {
            block.setState(Block.StateType.LIVE);
        }
    }

    @Override
    public void end() {
        time = 0f;
    }
}
