package src.world.entities.enemies.wheel.states;

import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.wheel.WheelEnemy;

public class IdleStateWheel extends StateEnemy<WheelEnemy>
{
    private boolean flip = false;

     public IdleStateWheel(WheelEnemy enemy)
    {
        super(enemy);
    }

    @Override
    public void start()
    {
        super.start();
        enemy.setAnimation(WheelEnemy.AnimationType.IDLE);
    }

    @Override
    public void update(Float delta) {
        if (!flip) {
            enemy.setFlipX(!enemy.getSprite().isFlipX());
            flip = true;
        }

        if (enemy.getActCrono() > 0.2f) {
            enemy.setState(WheelEnemy.StateType.WALK);
        }
    }

    @Override
    public void end() {
        flip = false;
    }
}
