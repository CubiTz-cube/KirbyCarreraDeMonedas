package src.world.entities.player.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.player.Player;

public class IdleState extends CanBasicMoveState{
    public IdleState(Player player){
        super(player);
    }

    @Override
    public void start() {
        if (player.enemyAbsorded == null) player.setAnimation(Player.AnimationType.IDLE);
        else player.setAnimation(Player.AnimationType.ABSORBIDLE);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x != 0) player.setState(Player.StateType.WALK);
        if(velocity.y < -1f) player.setState(Player.StateType.FALL);
    }

    @Override
    public void end() {

    }
}
