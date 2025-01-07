package src.world.entities.enemies.sword.states;

import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sword.SwordEnemy;

public class AttackStateSword extends StateEnemy<SwordEnemy>
{
    public AttackStateSword(SwordEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(SwordEnemy.AnimationType.ATTACK);
        enemy.throwEntity(Entity.Type.SWORDPROENEMY, 4f, 0f);
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
