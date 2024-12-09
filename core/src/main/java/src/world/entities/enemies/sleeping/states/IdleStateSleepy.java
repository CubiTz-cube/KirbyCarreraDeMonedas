package src.world.entities.enemies.sleeping.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class IdleStateSleepy extends StateEnemy<SleepingEnemy>
{

    public IdleStateSleepy(SleepingEnemy enemy) {
        super(enemy);
    }

    @Override
    public void update(Float delta) {

    }

    @Override
    public void end() {

    }
}
