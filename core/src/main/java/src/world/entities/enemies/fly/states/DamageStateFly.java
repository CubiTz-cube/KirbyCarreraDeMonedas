package src.world.entities.enemies.fly.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.fly.FlyEnemy;

public class DamageStateFly extends StateEnemy<FlyEnemy> {

    public DamageStateFly(FlyEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(FlyEnemy.AnimationType.DAMAGE);
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
