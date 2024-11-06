package src.world.entities.enemies.sleeping.states;

import com.badlogic.gdx.Gdx;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class WalkState extends StateEnemy
{
    private final float wakeUpTime = 5f;
    private float sleepTimer = 0f;

    public WalkState(StateMachine stateMachine, SleepingEnemy enemy)
    {
        super(stateMachine, enemy);
    }

    @Override
    public void start()
    {
        sleepTimer = 0f;
    }

    @Override
    public void update(Float delta)
    {
        // Mueve al enemigo y luego vuelve a caminar
        enemy.getBody().setLinearVelocity(3, enemy.getBody().getLinearVelocity().y);
        if (shouldSleep())
        {
            enemy.setState(SleepingEnemy.State.IDLE);
        }

        if (shouldSleep())
        {
            enemy.setState(SleepingEnemy.State.IDLE);
        }
    }

    private boolean shouldSleep()
    {
        //Duerme tras unos segundos
        sleepTimer += Gdx.graphics.getDeltaTime();
        return sleepTimer >= wakeUpTime;
    }

    @Override
    public void end()
    {
        // Dormir
        sleepTimer = 0f;
    }
}
