package src.world.entities.enemies.basic.states;

import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class IdleState extends StateEnemy {
    private boolean flip = false;

    public IdleState(StateMachine stateMachine, BasicEnemy enemy) {
        super(stateMachine, enemy);
    }

    @Override
    public void update(Float delta) {
        if (enemy.getActCrono() > 1 && !flip) {
            enemy.setFlipX(!enemy.getSprite().isFlipX());
            flip = true;
        }
        if (enemy.getActCrono() > 2) {
            enemy.setState(SleepingEnemy.State.WALK);
        }
    }

    @Override
    public void end() {
        flip = false;
    }
}
