package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.utils.variables.PlayerControl;
import src.world.player.Player;

public class WalkState extends RunState{
    public WalkState(Player player){
        super(player);
    }

    @Override
    public void start() {
        if (player.enemyAbsorded == null) player.setAnimation(Player.AnimationType.WALK);
        else player.setAnimation(Player.AnimationType.ABSORBWALK);

        player.speed = Player.WALK_SPEED;
        player.maxSpeed =  Player.WALK_MAX_SPEED;
        if (Math.abs(player.getBody().getLinearVelocity().x) > player.maxSpeed) player.setState(Player.StateType.RUN);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (Gdx.input.isKeyPressed(PlayerControl.RUN)){
            player.setState(Player.StateType.RUN);
        }
    }
}
