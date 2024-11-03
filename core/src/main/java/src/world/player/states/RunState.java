package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class RunState extends CanMoveState{
    public RunState(StateMachine stateMachine, Player player)
    {
        super(stateMachine, player);
    }

    @Override
    public void start()
    {
        player.getSprite().setColor(Color.YELLOW);
        player.speed = 15;
        player.maxSpeed = 6;
    }

    @Override
    public void update(Float delta)
    {
        super.update(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            stateMachine.setState(player.getDownState());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            stateMachine.setState(player.getJumpState());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            stateMachine.setState(player.getFlyState());
        }
        
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            stateMachine.setState(player.getIdleState());
        }
        if (velocity.y < -1)
        {
            stateMachine.setState(player.getFallState());
        }
    }

    @Override
    public void end()
    {
        player.getSprite().setColor(Color.WHITE);
    }
}
