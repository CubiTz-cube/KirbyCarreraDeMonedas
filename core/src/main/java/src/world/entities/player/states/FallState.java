package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.constants.PlayerControl;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class FallState extends CanMoveState {
    private Float fallForce;

    public FallState(Player player) {
        super(player);
        fallForce = 0f;
    }

    @Override
    public void start() {
        if (player.enemyAbsorded == null) player.setAnimation(Player.AnimationType.FALLSIMPLE);
        else player.setAnimation(Player.AnimationType.ABSORBFALL);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            player.doAction();
        }

        Vector2 velocity = player.getBody().getLinearVelocity();
        fallForce = Math.max(fallForce, Math.abs(velocity.y));
        if (velocity.y == 0){
            if (velocity.x == 0)  player.setCurrentState(Player.StateType.IDLE);
            else player.setCurrentState(Player.StateType.WALK);
        }
        if (Gdx.input.isKeyJustPressed(PlayerControl.JUMP) && player.enemyAbsorded == null){
            fallForce = 0f;
            player.setCurrentState(Player.StateType.FLY);
        }
    }

    @Override
    public void end() {
        float resultFallForce = fallForce / 2;
        if (resultFallForce > 14f) {
            player.game.addCameraShake(0.1f, resultFallForce);
            player.playSound(Player.SoundType.HEAVYFALL);
        }
        fallForce = 0f;
    }
}
