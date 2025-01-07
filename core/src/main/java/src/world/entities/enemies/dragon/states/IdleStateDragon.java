package src.world.entities.enemies.dragon.states;

import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.dragon.DragonEnemy;

public class IdleStateDragon extends StateEnemy<DragonEnemy> {
    private boolean flip = false;

    public IdleStateDragon(DragonEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(DragonEnemy.AnimationType.IDLE);
    }

    @Override
    public void update(Float delta) {
        /*if (!flip) {
            enemy.setFlipX(!enemy.getSprite().isFlipX());
            flip = true;
        }*/

        if (enemy.getActCrono() > 1.5f) {
            enemy.setState(Enemy.StateType.WALK);
        }
    }

    @Override
    public void end() {
        flip = false;
    }
}
