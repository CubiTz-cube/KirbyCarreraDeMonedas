package src.world.entities.enemies.wheel.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.wheel.WheelEnemy;

public class DamageStateWheel extends StateEnemy<WheelEnemy>
{
    public DamageStateWheel(WheelEnemy enemy)
    {
        super(enemy);
    }

    @Override
    public void start(){
        super.start();
        enemy.setAnimation(WheelEnemy.AnimationType.DAMAGE);
    }

    @Override
    public void update(Float delta) {
        if (enemy.isAnimationFinish()) {
            enemy.setState(WheelEnemy.StateType.IDLE);
            if (enemy.isDead()) enemy.game.removeEntity(enemy.getId());
        }
    }

    @Override
    public void end() {

    }
}
