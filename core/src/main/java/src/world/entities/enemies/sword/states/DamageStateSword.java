package src.world.entities.enemies.sword.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.basic.BasicEnemy;
import src.world.entities.enemies.sword.SwordEnemy;

public class DamageStateSword extends StateEnemy<SwordEnemy> {

    public DamageStateSword(SwordEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(SwordEnemy.AnimationType.DAMAGE);
    }

    @Override
    public void update(Float delta) {
        if (enemy.isAnimationFinish()) {
            enemy.setState(BasicEnemy.StateType.IDLE);
            if (enemy.isDead()) enemy.game.removeEntity(enemy.getId());
        }
    }

    @Override
    public void end() {

    }
}
