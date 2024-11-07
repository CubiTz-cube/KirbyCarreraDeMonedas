package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Fixture;
import src.utils.stateMachine.StateMachine;
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

        float velocityX = player.getBody().getLinearVelocity().x;
        if (velocityX != 0) {
            player.getBody().setLinearVelocity(velocityX * 0.90f, player.getBody().getLinearVelocity().y);
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.Z)){
            player.setState(Player.StateType.IDLE);
        }
    }

    @Override
    public void end() {

    }
}
