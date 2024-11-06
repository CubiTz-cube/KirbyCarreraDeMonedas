package src.world.entities.enemies;

import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;

public abstract class StateEnemy extends State {
    protected Enemy enemy;
    public StateEnemy(StateMachine stateMachine, Enemy enemy) {
        super(stateMachine);
        this.enemy = enemy;
    }

    @Override
    public void start() {
        enemy.setActCrono(0f);
    }
}
