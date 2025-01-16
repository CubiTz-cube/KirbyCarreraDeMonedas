package src.world.entities.enemies.turret.states;

import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.turret.TurretEnemy;

public class AttackStateTurret extends StateEnemy<TurretEnemy>
{
    public AttackStateTurret(TurretEnemy enemy)
    {
        super(enemy);
    }

    @Override
    public void start() {
        enemy.setAnimation(TurretEnemy.AnimationType.ATTACK);
        //enemy.throwEntity(Entity.Type.TURRETPROENEMY, 8f, 0f);
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
