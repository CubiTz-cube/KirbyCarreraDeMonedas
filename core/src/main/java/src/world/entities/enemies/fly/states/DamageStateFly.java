package src.world.entities.enemies.fly.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.fly.FlyEnemy;
import src.world.entities.enemies.sleeping.SleepingEnemy;

public class DamageStateFly extends StateEnemy<FlyEnemy> {

    public DamageStateFly(FlyEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(FlyEnemy.AnimationType.DAMAGE);
        enemy.getBody().setGravityScale(1);
    }

    @Override
    public void update(Float delta) {
        if (enemy.isAnimationFinish()) {
            enemy.setState(FlyEnemy.StateType.IDLE);
            if (enemy.isDead()) enemy.game.removeEntity(enemy.getId());
        }
    }

    @Override
    public void end() {

    }
}
