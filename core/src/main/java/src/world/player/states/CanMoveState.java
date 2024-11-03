package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public abstract class CanMoveState extends StatePlayer{
    public CanMoveState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void update(Float delta) {
        Body body = player.getBody();
        Vector2 velocity = body.getLinearVelocity();

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)){
            stateMachine.setState(player.getAbsorbState());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && velocity.x <  player.maxSpeed){
            body.applyForce( player.speed, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
            player.setFlipX(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && velocity.x > - player.maxSpeed){
            body.applyForce(- player.speed, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
            player.setFlipX(true);
        }
    }

}
