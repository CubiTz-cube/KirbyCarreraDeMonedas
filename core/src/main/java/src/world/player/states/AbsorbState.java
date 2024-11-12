package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import src.utils.variables.Constants;
import src.utils.stateMachine.StateMachine;
import src.utils.variables.PlayerControl;
import src.world.player.Player;

public class AbsorbState extends StatePlayer{
    public AbsorbState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.ABSORB);
    }

    @Override
    public void update(Float delta) {
        Fixture fix;

        if (player.getSprite().isFlipX()) fix = player.detectFrontFixture(-2.5f);
        else fix = player.detectFrontFixture(2.5f);
        if (fix != null) player.attractFixture(fix);

        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x != 0) {
            player.getBody().setLinearVelocity(velocity.x * 0.90f, player.getBody().getLinearVelocity().y);
        }

        if (!Gdx.input.isKeyPressed(PlayerControl.ACTION)){
            if (velocity.y != 0) player.setState(Player.StateType.FALL);
            else player.setState(Player.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
