package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.variables.PlayerControl;
import src.world.entities.player.Player;

public class WalkState extends CanBasicMoveState{
    public WalkState(Player player){
        super(player);
    }

    @Override
    public void start() {
        if (player.enemyAbsorded == null) player.setAnimation(Player.AnimationType.WALK);
        else player.setAnimation(Player.AnimationType.ABSORBWALK);

        player.speed = Player.WALK_SPEED;
        player.maxSpeed =  Player.WALK_MAX_SPEED;
        if (Math.abs(player.getBody().getLinearVelocity().x) > player.maxSpeed) player.setCurrentState(Player.StateType.RUN);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        if (Gdx.input.isKeyPressed(PlayerControl.RUN)){
            player.setCurrentState(Player.StateType.RUN);
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0 && !Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            player.setCurrentState(Player.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
