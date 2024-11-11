package src.world.entities.enemies.sleeping.states;

import com.badlogic.gdx.Gdx;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class IdleStateSleepy extends StateEnemy
{
    private float sleepTimer = 0f;

    public IdleStateSleepy(StateMachine stateMachine, Enemy enemy) {
        super(stateMachine, enemy);
    }


    @Override
    public void start()
    {
        float sleepTimer = 0f;
    }

    @Override
    public void update(Float delta)
    {
        if (shouldWakeUp())
        {
            enemy.setState(SleepingEnemy.StateType.WALK);
        }
    }

    private boolean shouldWakeUp()
    {
        sleepTimer += Gdx.graphics.getDeltaTime();
        return sleepTimer >= 20f;
    }

    @Override
    public void end()
    {
        sleepTimer = 0f;
    }


}
