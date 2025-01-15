package src.world.entities.enemies.turret.states;

import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.turret.TurretEnemy;

public class IdleStateTurret extends StateEnemy<TurretEnemy>
{
    public IdleStateTurret(TurretEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        enemy.setAnimation(TurretEnemy.AnimationType.IDLE);

    }

    @Override
    public void update(Float delta) {
        if (enemy.getActCrono() > 1) {
            enemy.setState(Enemy.StateType.ATTACK);
        }
    }

    @Override
    public void end() {

    }
}
