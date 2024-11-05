package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class RunState extends CanMoveState{
    public RunState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.setCurrentAnimation(player.getRunAnimation());
        player.speed = 15;
        player.maxSpeed = 6;
        player.getBody().applyLinearImpulse(player.isFlipX() ? -3 : 3, 0, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            stateMachine.setState(player.getDownState());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            stateMachine.setState(player.getJumpState());
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            stateMachine.setState(player.getIdleState());
        }
        if (velocity.y < -1){
            stateMachine.setState(player.getFallState());
        }
    }

    @Override
    public void end() {

    }
}
