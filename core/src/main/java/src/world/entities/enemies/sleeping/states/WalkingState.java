package src.world.entities.enemies.sleeping.states;

import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class WalkingState extends State
{
    private final SleepingEnemy enemy;

    public WalkingState(StateMachine stateMachine, SleepingEnemy enemy)
    {
        super(stateMachine);
        this.enemy = enemy;
    }

    @Override
    public void start()
    {
        // Camina

    }

    @Override
    public void update(Float delta)
    {
        // Mueve al enemigo y luego vuelve a caminar
        enemy.walk(delta);
        if (enemy.shouldSleep())
        {
            stateMachine.setState(enemy.getSleepingState());
        }
    }

    @Override
    public void end()
    {
        // Dormir
    }
}
