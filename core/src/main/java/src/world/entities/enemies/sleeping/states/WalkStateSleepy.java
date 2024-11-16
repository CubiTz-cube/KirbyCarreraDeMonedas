package src.world.entities.enemies.sleeping.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class WalkStateSleepy extends StateEnemy<SleepingEnemy>
{
    private float sleepTimer = 0f;
    private boolean flip = false;
    private float flipTimer = 0f;

    public WalkStateSleepy(SleepingEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start()
    {
        sleepTimer = 0f;
    }

    @Override
    public void update(Float delta)
    {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed)
        {
            enemy.getBody().applyForce(enemy.getSprite().isFlipX()? -5 : 5, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);

            if (flipTimer == 0f)
            {
                enemy.getSprite().flip(true, false);
                flip = !flip;
                flipTimer = 150f;
            }
            else
            {
                flipTimer--;
            }
        }

        if (shouldSleep())
        {
            enemy.setState(SleepingEnemy.StateType.IDLE);
        }
    }

    private boolean shouldSleep()
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
