package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class FallState extends StatePlayer{
    public FallState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.PURPLE);
    }

    @Override
    public void update(Float delta) {
        if (player.getBody().getLinearVelocity().y == 0){
            stateMachine.setState(player.getIdleState());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            stateMachine.setState(player.getFlyState());
        }
    }

    @Override
    public void end() {

    }
}
