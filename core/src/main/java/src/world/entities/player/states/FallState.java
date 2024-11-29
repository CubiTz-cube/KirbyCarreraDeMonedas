package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.variables.PlayerControl;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class FallState extends CanMoveState
{

    public FallState(Player player) {
        super(player);
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
        if (velocity.y == 0){
            if (velocity.x == 0)  player.setCurrentState(Player.StateType.IDLE);
            else player.setCurrentState(Player.StateType.WALK);
        }
        if (Gdx.input.isKeyJustPressed(PlayerControl.JUMP) && player.enemyAbsorded == null){
            player.setCurrentState(Player.StateType.FLY);
        }
    }

    @Override
    public void end()
    {}
}
