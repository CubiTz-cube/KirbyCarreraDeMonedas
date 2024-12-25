package src.world.entities.enemies.wheel.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.wheel.WheelEnemy;

public class WalkStateWheel extends StateEnemy<WheelEnemy>
{
    public WalkStateWheel(WheelEnemy enemy)
    {
        super(enemy);
    }

    @Override
    public void start()
    {
        super.start();
        enemy.setAnimation(WheelEnemy.AnimationType.WALK);
    }

    @Override
    public void update(Float delta) {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed) {
            enemy.getBody().applyForce(enemy.getSprite().isFlipX()? -15 : 15, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);
        }

        if (enemy.getActCrono() > 2) {
            enemy.setState(Enemy.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
