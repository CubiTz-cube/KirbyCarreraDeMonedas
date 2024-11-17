package src.world.entities.enemies.sleeping.states;

import com.badlogic.gdx.Gdx;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class IdleStateSleepy extends StateEnemy<SleepingEnemy>
{
    private float sleepTimer = 0f;

    public IdleStateSleepy(SleepingEnemy enemy) {
        super(enemy);
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
        return sleepTimer >= 10f;
    }

    @Override
    public void end()
    {
        sleepTimer = 0f;
    }


}
