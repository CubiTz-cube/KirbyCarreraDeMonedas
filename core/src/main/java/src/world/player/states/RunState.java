package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class RunState extends StatePlayer{
    public RunState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.YELLOW);
        player.speed = 15;
        player.maxSpeed = 6;
    }

    @Override
    public void update(Float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            stateMachine.setState(player.getJumpState());
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
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
