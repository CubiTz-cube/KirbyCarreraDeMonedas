package src.world.entities.enemies.dragon.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.dragon.DragonEnemy;

public class WalkStateDragon extends StateEnemy<DragonEnemy> {

    public WalkStateDragon(DragonEnemy enemy) {
        super(enemy);
    }
    @Override
    public void start()
    {
        super.start();
        enemy.setAnimation(DragonEnemy.AnimationType.WALK);
    }

    @Override
    public void update(Float delta)
    {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed) {
            enemy.getBody().applyForce(enemy.getSprite().isFlipX()? -10 : 10, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);
        }

        if (enemy.getActCrono() > 0.5f){
            enemy.setState(Enemy.StateType.ATTACK);
        }
    }

    @Override
    public void end() {

    }
}
