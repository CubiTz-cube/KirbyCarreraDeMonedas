package src.world.entities.enemies.dragon.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.dragon.DragonEnemy;

public class DamageStateDragon extends StateEnemy<DragonEnemy> {

    public DamageStateDragon(DragonEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(DragonEnemy.AnimationType.DAMAGE);
    }

    @Override
    public void update(Float delta) {
        if (enemy.isAnimationFinish()) {
            enemy.setState(DragonEnemy.StateType.IDLE);
            if (enemy.isDead()) enemy.game.removeEntity(enemy.getId());
        }
    }

    @Override
    public void end() {

    }
}
