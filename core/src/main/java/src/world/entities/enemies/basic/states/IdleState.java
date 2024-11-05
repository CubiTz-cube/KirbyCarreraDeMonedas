package src.world.entities.enemies.basic.states;

import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.basic.BasicEnemy;

public class IdleState extends StateBasicEnemy {
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
            stateMachine.setState(enemy.getWalkState());
        }
    }

    @Override
    public void end() {
        flip = false;
    }
}
