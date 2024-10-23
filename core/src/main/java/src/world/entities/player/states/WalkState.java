package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.entities.player.Player;

public class WalkState extends StatePlayer{
    public WalkState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.GREEN);
    }

    @Override
    public void update(Float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            stateMachine.setState(player.getJumpState());
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0){
            stateMachine.setState(player.getIdleState());
        }
        if (velocity.y < 0){
            stateMachine.setState(player.getFallState());
        }
    }

    @Override
    public void end() {

    }
}
