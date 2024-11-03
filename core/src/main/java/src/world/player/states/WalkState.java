package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class WalkState extends State
{
    private final Player player;

    public WalkState(StateMachine stateMachine, Player player)
    {
        super(stateMachine);
        this.player = player;
    }

    @Override
    public void start() {
        player.setCurrentAnimation(player.getWalkAnimation());
        player.speed = 10;
        player.maxSpeed = 4;
        if (Math.abs(player.getBody().getLinearVelocity().x) > player.maxSpeed) stateMachine.setState(player.getRunState());
    }

    @Override
    public void update(Float delta)
    {
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

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            stateMachine.setState(player.getFlyState());
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            stateMachine.setState(player.getJumpState());
        }
        else if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            stateMachine.setState(player.getIdleState());
        }
    }

    @Override
    public void end()
    {}
}
