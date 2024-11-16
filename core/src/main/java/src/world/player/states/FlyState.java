package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.utils.variables.PlayerControl;
import src.world.player.Player;

public class FlyState extends CanMoveState{
    Float time = 0f;

    public FlyState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.FLY);
        player.speed = Player.WALK_SPEED;
        player.maxSpeed = Player.WALK_MAX_SPEED;
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().setGravityScale(0.6f);
        player.getBody().applyLinearImpulse(0, Player.FLY_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        if (player.isAnimationFinish() && player.getCurrentAnimationType() == Player.AnimationType.FLY) player.setAnimation(Player.AnimationType.INFLY);
        time += delta;
        if (Gdx.input.isKeyPressed(PlayerControl.JUMP) && time > 0.2f){
            if (player.getCurrentAnimationType() != Player.AnimationType.FLY) player.setAnimation(Player.AnimationType.UPFLY);
            time = 0f;
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(0, Player.FLY_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            if (player.getCurrentAnimationType() != Player.AnimationType.FLYEND) player.setAnimation(Player.AnimationType.FLYEND);
        }
        if (player.isAnimationFinish() && player.getCurrentAnimationType() == Player.AnimationType.FLYEND) player.setState(Player.StateType.FALL);
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
