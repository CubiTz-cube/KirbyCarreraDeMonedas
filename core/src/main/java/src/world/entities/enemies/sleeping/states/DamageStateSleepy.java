package src.world.entities.enemies.sleeping.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class DamageStateSleepy extends StateEnemy<SleepingEnemy> {

    public DamageStateSleepy(SleepingEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(Float delta) {
        enemy.setState(BasicEnemy.StateType.IDLE);
        if (enemy.isDead()) enemy.game.removeEntity(enemy.getId());
    }

    @Override
    public void end() {

    }
}
