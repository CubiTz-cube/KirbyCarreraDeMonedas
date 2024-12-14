package src.world.entities.enemies.sword.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sword.SwordEnemy;

public class WalkStateSword extends StateEnemy<SwordEnemy>
{

    public WalkStateSword(SwordEnemy enemy)
    {
        super(enemy);
    }

    @Override
    public void start()
    {
        super.start();
        enemy.setAnimation(SwordEnemy.AnimationType.WALK);
    }

    @Override
    public void update(Float delta)
    {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed) {
            enemy.getBody().applyForce(enemy.getSprite().isFlipX()? -5 : 5, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);
        }

        if (enemy.getActCrono() > 3) {
            enemy.setState(Enemy.StateType.ATTACK);
        }
    }

    @Override
    public void end() {

    }
}
