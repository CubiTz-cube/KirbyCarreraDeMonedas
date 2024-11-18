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
    private float flipTimer = 0f;

    public WalkStateSleepy(SleepingEnemy enemy) {
        super(enemy);
    }

    @Override
    public void update(Float delta)
    {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed)
        {
            enemy.getBody().applyForce(enemy.isFlipX()? -8 : 8, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);

            if (flipTimer == 0f)
            {
                enemy.setFlipX(!enemy.isFlipX());
                flipTimer = 15f;
            }
            else
            {
                flipTimer -= delta;
            }
        }

        if (enemy.getActCrono() >= 10f)
        {
            enemy.setState(SleepingEnemy.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
