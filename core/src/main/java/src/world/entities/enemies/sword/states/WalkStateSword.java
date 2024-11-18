package src.world.entities.enemies.sword.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sword.SwordEnemy;

public class WalkStateSword extends StateEnemy<SwordEnemy>
{
    private boolean flip = false;
    private float flipTimer = 0f;

    public WalkStateSword(SwordEnemy enemy)
    {
        super(enemy);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void update(Float delta)
    {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed)
        {
            enemy.getBody().applyForce(enemy.getSprite().isFlipX() ? -5 : 5, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);

            if (flipTimer == 0f)
            {
                enemy.getSprite().flip(true, false);
                flip = !flip;
                flipTimer = 100f;
            }
            else
            {
                flipTimer--;
            }

        }

        /*if (shouldAttack())
        {
            enemy.setState(SwordEnemy.StateType.ATTACK);
        }*/
    }

    @Override
    public void end()
    {

    }
}
