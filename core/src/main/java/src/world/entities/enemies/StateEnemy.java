package src.world.entities.enemies;

import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;

public abstract class StateEnemy implements State {
    protected Enemy enemy;
    public StateEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void start() {
        enemy.setActCrono(0f);
    }
}
