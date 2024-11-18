package src.world.entities.enemies.sleeping.states;

import com.badlogic.gdx.Gdx;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class IdleStateSleepy extends StateEnemy<SleepingEnemy>
{

    public IdleStateSleepy(SleepingEnemy enemy) {
        super(enemy);
    }

    @Override
    public void update(Float delta)
    {
        if (enemy.getActCrono() >= 3f)
        {
            enemy.setState(SleepingEnemy.StateType.WALK);
        }
    }

    @Override
    public void end() {

    }
}
