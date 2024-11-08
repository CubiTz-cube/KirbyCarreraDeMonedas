package src.world.player.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class StuntState extends StatePlayer{
    private Float time = 0f;

    public StuntState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        time = 0f;
        player.getSprite().setColor(Color.BLUE);
    }

    @Override
    public void update(Float delta) {
        time += delta;
        if (time > 2f) {
            player.setState(Player.StateType.IDLE);
        }
    }

    @Override
    public void end() {
        player.getSprite().setColor(Color.WHITE);
    }
}
