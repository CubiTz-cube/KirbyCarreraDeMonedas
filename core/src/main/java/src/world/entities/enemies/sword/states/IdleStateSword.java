package src.world.entities.enemies.sword.states;

import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sword.SwordEnemy;

public class IdleStateSword  extends StateEnemy<SwordEnemy>
{

    private boolean flip = false;
    public IdleStateSword(SwordEnemy enemy) {
        super(enemy);
    }

    @Override
    public void update(Float delta) {
        if (enemy.getActCrono() > 1 && !flip) {
            enemy.setFlipX(!enemy.getSprite().isFlipX());
            flip = true;
        }
        else if (enemy.getActCrono() > 2) {
            enemy.setState(Enemy.StateType.WALK);
        }
    }

    @Override
    public void end() {

    }
}
