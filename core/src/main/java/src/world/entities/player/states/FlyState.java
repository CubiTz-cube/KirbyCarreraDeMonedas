package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.entities.player.Player;

public class FlyState extends StatePlayer{
    public FlyState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.RED);
        player.getSprite().setScale(1.1f);
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        player.getBody().setGravityScale(0.6f);
    }

    @Override
    public void update(Float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            stateMachine.setState(player.getFallState());
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.y < -5) {
            player.getBody().setLinearVelocity(velocity.x, -5);
        }
    }

    @Override
    public void end() {
        player.getBody().setGravityScale(1);
        player.getSprite().setScale(1);
    }
}
