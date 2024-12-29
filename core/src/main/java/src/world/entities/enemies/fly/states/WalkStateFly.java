package src.world.entities.enemies.fly.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.fly.FlyEnemy;

public class WalkStateFly extends StateEnemy<FlyEnemy> {

    public WalkStateFly(FlyEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(FlyEnemy.AnimationType.WALK);
    }

    @Override
    public void update(Float delta) {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed) {
            enemy.getBody().applyForce(0, enemy.getSprite().isFlipX()? -7 : 7,
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
