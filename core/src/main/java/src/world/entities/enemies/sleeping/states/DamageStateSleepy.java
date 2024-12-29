package src.world.entities.enemies.sleeping.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class DamageStateSleepy extends StateEnemy<SleepingEnemy> {

    public DamageStateSleepy(SleepingEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(SleepingEnemy.AnimationType.DAMAGE);
    }

    @Override
    public void update(Float delta) {
        enemy.setState(SleepingEnemy.StateType.IDLE);
        if (enemy.isDead()) enemy.game.removeEntity(enemy.getId());
    }

    @Override
    public void end() {

    }
}
