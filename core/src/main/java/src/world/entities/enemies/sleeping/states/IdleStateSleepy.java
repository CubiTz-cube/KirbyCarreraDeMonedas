package src.world.entities.enemies.sleeping.states;

import com.badlogic.gdx.Gdx;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class IdleStateSleepy extends StateEnemy
{
    private final float wakeUpTime = 5f;
    private float sleepTimer = 0f;

    public IdleStateSleepy(StateMachine stateMachine, Enemy enemy) {
        super(stateMachine, enemy);
    }


    @Override
    public void start()
    {
        // Dormir
        float sleepTimer = 0f;
    }

    @Override
    public void update(Float delta)
    {
        // Duerme un poquito y se despierta
        if (shouldWakeUp())
        {
            enemy.setState(SleepingEnemy.StateType.WALK);
        }
    }

    private boolean shouldWakeUp()
    {
        //Despierta tras unos segundos
        sleepTimer += Gdx.graphics.getDeltaTime();
        return sleepTimer >= wakeUpTime;
    }

    @Override
    public void end()
    {
        // Despertar y comenzar a caminar un poco antes de volver a dormir

    }


}
