package src.world.entities.enemies.sword.states;

import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sword.SwordEnemy;

public class IdleStateSword  extends StateEnemy<SwordEnemy>
{

    private boolean flip = false;

    public IdleStateSword(SwordEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(SwordEnemy.AnimationType.IDLE);
    }

    @Override
    public void update(Float delta) {
        if (!flip) {
            enemy.setFlipX(!enemy.getSprite().isFlipX());
            flip = true;
        }

        if (enemy.getActCrono() > 1) {
            enemy.setState(Enemy.StateType.WALK);
        }
    }

    @Override
    public void end() {
        flip = false;
    }
}
