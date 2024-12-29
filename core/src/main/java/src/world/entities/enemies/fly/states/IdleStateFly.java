package src.world.entities.enemies.fly.states;

import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.fly.FlyEnemy;

public class IdleStateFly extends StateEnemy<FlyEnemy> {

    private boolean flip = false;

    public IdleStateFly(FlyEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(FlyEnemy.AnimationType.IDLE);
    }

    @Override
    public void update(Float delta) {
        if (enemy.getActCrono() > 1 && !flip) {
            enemy.setFlipX(!enemy.getSprite().isFlipX());
            flip = true;
        }

        if (enemy.getActCrono() > 0.01f) {
            enemy.setState(Enemy.StateType.WALK);
        }
    }

    @Override
    public void end() {
        flip = false;
    }
}
