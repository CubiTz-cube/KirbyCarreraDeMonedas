package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class JumpState extends State
{
    private final Player player;
    private float jumpTimeCounter = 0f;
    private boolean isJumping = false;

    public JumpState(StateMachine stateMachine, Player player)
    {
        super(stateMachine);
        this.player = player;
    }

    @Override
    public void start()
    {
        isJumping = true;
        jumpTimeCounter = 0f;
        player.getBody().applyLinearImpulse(new Vector2(0, Player.JUMP_IMPULSE), player.getBody().getWorldCenter(), true);
    }

    @Override
    public void update(Float delta)
    {
        jumpTimeCounter += delta;

        float horizontalForce = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            horizontalForce = -player.speed;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            horizontalForce = player.speed;
        }
        player.getBody().setLinearVelocity(horizontalForce, player.getBody().getLinearVelocity().y);

        if (isJumping && Gdx.input.isKeyPressed(Input.Keys.UP) && jumpTimeCounter < Player.MAX_JUMP_TIME)
        {
            player.getBody().applyLinearImpulse(new Vector2(0, Player.JUMP_IMPULSE * delta), player.getBody().getWorldCenter(), true);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            stateMachine.setState(player.getFlyState());
        }
        else
        {
            isJumping = false;
            stateMachine.setState(player.getFallState());
        }
    }

    @Override
    public void end()
    {
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
    }
}
