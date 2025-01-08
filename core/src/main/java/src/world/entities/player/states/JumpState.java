package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import src.utils.constants.PlayerControl;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class JumpState extends CanMoveState{
    private Float jumpTime = 0f;

    public JumpState(Player player){
        super(player);
    }

    @Override
    public void start() {
        if (player.isEnemyAbsorb()) player.setAnimation(Player.AnimationType.ABSORBJUMP);
        else player.setAnimation(Player.AnimationType.JUMP);
        player.playSound(Player.SoundType.JUMP);
        jumpTime = 0f;
        player.getBody().applyLinearImpulse(0, Player.JUMP_IMPULSE, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION)){
            player.doAction();
        }

        if (jumpTime < Player.MAX_JUMP_TIME && Gdx.input.isKeyPressed(PlayerControl.JUMP)){
            jumpTime += delta;
            player.getBody().applyLinearImpulse(0, Player.JUMP_INAIR * delta, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyJustPressed(PlayerControl.JUMP) && !player.isEnemyAbsorb()){
            player.setCurrentState(Player.StateType.FLY);
        }
        if (player.getBody().getLinearVelocity().y < 0){
            player.setCurrentState(Player.StateType.FALL);
            if (player.isEnemyAbsorb()) player.setAnimation(Player.AnimationType.ABSORBFALL);
            else player.setAnimation(Player.AnimationType.FALL);
        }
    }

    @Override
    public void end() {

    }
}
