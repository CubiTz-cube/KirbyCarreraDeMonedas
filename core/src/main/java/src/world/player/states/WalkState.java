package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class WalkState extends RunState{
    public WalkState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.GREEN);
        player.speed = 10;
        player.maxSpeed = 4;
        if (Math.abs(player.getBody().getLinearVelocity().x) > player.maxSpeed) stateMachine.setState(player.getRunState());
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        Vector2 velocity = player.getBody().getLinearVelocity();
        if ((Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && velocity.x < 0)
            || (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) && velocity.x > 0){
            stateMachine.setState(player.getRunState());
        }
    }
}
