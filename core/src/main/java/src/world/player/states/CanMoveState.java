package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import src.utils.variables.Constants;
import src.utils.stateMachine.StateMachine;
import src.utils.variables.PlayerControl;
import src.world.player.Player;

public abstract class CanMoveState extends StatePlayer{
    public CanMoveState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void update(Float delta) {
        Body body = player.getBody();
        Vector2 velocity = body.getLinearVelocity();

        if (Gdx.input.isKeyPressed(PlayerControl.RIGHT) && velocity.x <  player.maxSpeed){
            body.applyForce( player.speed, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
            if (player.isFlipX()) player.setFlipX(false);
        }
        if (Gdx.input.isKeyPressed(PlayerControl.LEFT) && velocity.x > - player.maxSpeed){
            body.applyForce(- player.speed, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
            if (!player.isFlipX()) player.setFlipX(true);
        }
    }

}
