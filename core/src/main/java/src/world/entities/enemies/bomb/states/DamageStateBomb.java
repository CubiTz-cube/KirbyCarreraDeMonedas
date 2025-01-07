package src.world.entities.enemies.bomb.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.bomb.BombEnemy;

public class DamageStateBomb extends StateEnemy<BombEnemy> {

    public DamageStateBomb(BombEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(BombEnemy.AnimationType.DAMAGE);
    }

    @Override
    public void update(Float delta) {
        if (enemy.isAnimationFinish()) {
            enemy.setState(BombEnemy.StateType.IDLE);
            if (enemy.isDead()) enemy.game.removeEntity(enemy.getId());
        }
    }

    @Override
    public void end() {

    }
}
