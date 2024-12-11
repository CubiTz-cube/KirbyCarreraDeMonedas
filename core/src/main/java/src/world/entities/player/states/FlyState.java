package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.constants.PlayerControl;
import src.world.entities.Entity;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

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
        time += delta;

        if (player.isAnimationFinish() && player.getCurrentAnimationType() == Player.AnimationType.FLY) player.setAnimation(Player.AnimationType.FLYIN);

        if (Gdx.input.isKeyPressed(PlayerControl.JUMP) && time > 0.2f){
            if (player.getCurrentAnimationType() != Player.AnimationType.FLY) player.setAnimation(Player.AnimationType.FLYUP);
            time = 0f;
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(0, Player.FLY_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (player.isAnimationFinish() && player.getCurrentAnimationType() == Player.AnimationType.FLYUP) player.setAnimation(Player.AnimationType.FLYIN);

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            if (player.getCurrentAnimationType() != Player.AnimationType.FLYEND) {
                player.setAnimation(Player.AnimationType.FLYEND);
                player.throwEntity(Entity.Type.CLOUD, 1.5f);
                player.playSound(PlayerCommon.soundType.AIRSHOT);
            }
        }

        if (player.isAnimationFinish() && player.getCurrentAnimationType() == Player.AnimationType.FLYEND) player.setCurrentState(Player.StateType.FALL);

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
