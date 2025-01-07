package src.world.entities.enemies.bomb.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.bomb.BombEnemy;

public class WalkStateBomb extends StateEnemy<BombEnemy> {

    public WalkStateBomb(BombEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start()
    {
        super.start();
        enemy.setAnimation(BombEnemy.AnimationType.WALK);
    }

    @Override
    public void update(Float delta)
    {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed) {
            enemy.getBody().applyForce(enemy.getSprite().isFlipX()? -5 : 5, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);
        }

        if (enemy.getActCrono() > 1f){
            enemy.setState(Enemy.StateType.ATTACK);
        }
    }

    @Override
    public void end() {

    }
}
