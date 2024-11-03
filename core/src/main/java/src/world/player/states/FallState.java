package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class FallState extends State
{
    private final Player player;

    public FallState(StateMachine stateMachine, Player player)
    {
        super(stateMachine);
        this.player = player;
    }

    @Override
    public void start() {
        player.setCurrentAnimation(player.getFallAnimation());
    }

    @Override
    public void update(Float delta)
    {
        float horizontalForce = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce = -player.speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce = player.speed;
        }
        player.getBody().setLinearVelocity(horizontalForce, player.getBody().getLinearVelocity().y);

        if (player.isOnGround())
        {
            stateMachine.setState(player.getIdleState());
        }
        else if (player.getBody().getLinearVelocity().y > 0)
        {
            stateMachine.setState(player.getJumpState());
        }
    }

    @Override
    public void end()
    {}
}
