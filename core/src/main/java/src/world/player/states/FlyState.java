package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class FlyState extends State
{
    private final Player player;

    public FlyState(StateMachine stateMachine, Player player)
    {
        super(stateMachine);
        this.player = player;
    }

    @Override
    public void start()
    {}

    @Override
    public void update(Float delta)
    {
        float horizontalForce = 0f;
        float verticalForce = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            horizontalForce = -player.speed;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            horizontalForce = player.speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            verticalForce = player.speed;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            verticalForce = -player.speed;
        }

        player.getBody().setLinearVelocity(horizontalForce, verticalForce);

        if (player.isOnGround())
        {
            stateMachine.setState(player.getIdleState());
        }
    }

    @Override
    public void end()
    {}
}
