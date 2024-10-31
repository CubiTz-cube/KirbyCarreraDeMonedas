package src.world.entities.enemies.sleeping.states;

import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class SleepingState extends State
{
    private final SleepingEnemy enemy;

    public SleepingState(StateMachine stateMachine, SleepingEnemy enemy)
    {
        super(stateMachine);
        this.enemy = enemy;
    }

    @Override
    public void start()
    {
        // Dormir
    }


    @Override
    public void update(Float delta)
    {
        // Duerme un poquito y se despierta
        if (enemy.shouldWakeUp())
        {
            stateMachine.setState(enemy.getWalkingState());
        }
    }

    @Override
    public void end()
    {
        // Despertar y comenzar a caminar un poco antes de volver a dormir
    }
}
