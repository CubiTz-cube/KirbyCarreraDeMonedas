package src.world.entities.enemies.basic.states;

import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.basic.BasicEnemy;

public abstract class StateBasicEnemy extends State {
    protected BasicEnemy enemy;
    public StateBasicEnemy(StateMachine stateMachine, BasicEnemy enemy) {
        super(stateMachine);
        this.enemy = enemy;
    }

    @Override
    public void start() {
        enemy.resetActCrono();
    }
}
