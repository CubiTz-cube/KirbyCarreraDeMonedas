package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class IdleState extends State
{
    private final Player player;

    public IdleState(StateMachine stateMachine, Player player)
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
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            stateMachine.setState(player.getWalkState());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            stateMachine.setState(player.getFlyState());
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            stateMachine.setState(player.getJumpState());
        }
    }

    @Override
    public void end()
    {}
}
