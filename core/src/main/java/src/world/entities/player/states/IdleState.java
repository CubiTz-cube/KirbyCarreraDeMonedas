package src.world.entities.player.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.player.Player;

public class IdleState extends CanBasicMoveState{
    public IdleState(Player player){
        super(player);
    }

    @Override
    public void start() {
        if (player.isEnemyAbsorb()) player.setAnimation(Player.AnimationType.ABSORBIDLE);
        else player.setAnimation(Player.AnimationType.IDLE);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        Vector2 velocity = player.getBody().getLinearVelocity();
        if (Math.abs(velocity.x) > 0.1f) player.setCurrentState(Player.StateType.WALK);
    }

    @Override
    public void end() {

    }
}
