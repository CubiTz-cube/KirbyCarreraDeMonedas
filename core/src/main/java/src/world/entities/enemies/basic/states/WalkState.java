package src.world.entities.enemies.basic.states;

import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.entities.enemies.basic.BasicEnemy;

public class WalkState extends StateBasicEnemy {
    public WalkState(StateMachine stateMachine, BasicEnemy enemy) {
        super(stateMachine, enemy);
    }

    @Override
    public void update(Float delta) {
        Vector2 velocity = enemy.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) < enemy.speed) {
            enemy.getBody().applyForce(enemy.getSprite().isFlipX()? -5 : 5, 0,
                enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);
        }

        if (enemy.getActCrono() > 3) {
            stateMachine.setState(enemy.getIdleState());
        }
    }

    @Override
    public void end() {

    }
}
