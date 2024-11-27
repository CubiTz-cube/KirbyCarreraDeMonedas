package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import src.utils.variables.PlayerControl;
import src.world.entities.player.Player;

public class JumpState extends CanMoveState{
    private Float jumpTime = 0f;

    public JumpState(Player player){
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.JUMP);
        jumpTime = 0f;
        player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            if (player.enemyAbsorded == null) player.setCurrentState(Player.StateType.ABSORB);
            else player.setCurrentState(Player.StateType.STAR);
        }

        if (jumpTime < Player.MAX_JUMP_TIME && Gdx.input.isKeyPressed(PlayerControl.JUMP)){
            jumpTime += delta;
            player.getBody().applyLinearImpulse(0, Player.JUMP_INAIR, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyJustPressed(PlayerControl.JUMP) && player.enemyAbsorded == null){
            player.setCurrentState(Player.StateType.FLY);
        }
        if (player.getBody().getLinearVelocity().y < 0){
            player.setCurrentState(Player.StateType.FALL);
            player.setAnimation(Player.AnimationType.FALL);
        }
    }

    @Override
    public void end() {

    }
}
