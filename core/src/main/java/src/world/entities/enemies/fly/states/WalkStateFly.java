package src.world.entities.enemies.fly.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.fly.FlyEnemy;

public class WalkStateFly extends StateEnemy<FlyEnemy> {

    public WalkStateFly(FlyEnemy enemy) {
        super(enemy);
        enemy.getBody().setGravityScale(0);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(FlyEnemy.AnimationType.WALK);
    }

    @Override
    public void update(Float delta) {
        enemy.getBody().applyForce(0, enemy.flyDown? -1 : 1,
            enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);

        if (enemy.getActCrono() > 3) {
            enemy.setState(Enemy.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
