package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.world.player.Player;
import src.utils.stateMachine.StateMachine;

public class IdleState extends StatePlayer{
    public IdleState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.WHITE);
    }

    @Override
    public void update(Float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            stateMachine.setState(player.getJumpState());
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x != 0) stateMachine.setState(player.getWalkState());
        if(velocity.y < -1f) stateMachine.setState(player.getFallState());
    }

    @Override
    public void end() {

    }
}
