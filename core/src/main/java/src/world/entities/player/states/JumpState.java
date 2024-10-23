package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import src.world.entities.player.Player;
import src.utils.stateMachine.StateMachine;

public class JumpState extends StatePlayer{
    private Float jumpTime = 0f;

    public JumpState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.BLUE);
        jumpTime = 0f;
        player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        if (jumpTime < Player.MAX_JUMP_TIME && Gdx.input.isKeyPressed(Input.Keys.UP)){
            jumpTime += delta;
            player.getBody().applyLinearImpulse(0, Player.JUMP_INAIR, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            stateMachine.setState(player.getFlyState());
        }
        if (player.getBody().getLinearVelocity().y < 0){
            stateMachine.setState(player.getFallState());
        }
    }

    @Override
    public void end() {

    }
}
