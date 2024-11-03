package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class AbsorbState extends StatePlayer{
    public AbsorbState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.VIOLET);
    }

    @Override
    public void update(Float delta) {
        if (!Gdx.input.isKeyPressed(Input.Keys.X)){
            stateMachine.setState(player.getIdleState());
        }
    }

    @Override
    public void end() {

    }
}
