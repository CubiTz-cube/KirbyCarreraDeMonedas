package src.world.entities.enemies.sword.states;

import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.sword.SwordEnemy;

public class IdleStateSword  extends StateEnemy<SwordEnemy> {
    public IdleStateSword(SwordEnemy enemy) {
        super(enemy);
    }

    @Override
    public void update(Float delta) {
        enemy.setState(Enemy.StateType.WALK);
    }

    @Override
    public void end() {

    }
}
