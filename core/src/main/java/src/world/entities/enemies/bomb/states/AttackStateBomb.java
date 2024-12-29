package src.world.entities.enemies.bomb.states;

import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.bomb.BombEnemy;

public class AttackStateBomb extends StateEnemy<BombEnemy> {

    public AttackStateBomb(BombEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(BombEnemy.AnimationType.ATTACK);
        enemy.throwEntity(Entity.Type.BOMB, 4f,8f);
    }

    @Override
    public void update(Float delta) {
        if (enemy.getActCrono() > 1) {
            enemy.setState(Enemy.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }

}
