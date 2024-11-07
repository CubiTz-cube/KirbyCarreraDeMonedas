package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class FlyState extends CanMoveState{
    Float time = 0f;

    public FlyState(StateMachine stateMachine, Player player) {
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.FLY);
        player.speed = 10;
        player.maxSpeed = 4;
        player.getSprite().setScale(1.1f);
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().setGravityScale(0.6f);
        player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        time += delta;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && time > 0.2f){
            time = 0f;
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            player.setState(Player.StateType.FALL);
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
