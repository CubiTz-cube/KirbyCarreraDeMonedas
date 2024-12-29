package src.world.entities.enemies.dragon.states;

import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.dragon.DragonEnemy;

public class AttackStateDragon extends StateEnemy<DragonEnemy> {

    public AttackStateDragon(DragonEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(DragonEnemy.AnimationType.ATTACK);
    }

    @Override
    public void update(Float delta) {
        enemy.throwEntity(Entity.Type.ICE, 8f,0f);

        if (enemy.getActCrono() > 0.5f) {
            enemy.setState(Enemy.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
