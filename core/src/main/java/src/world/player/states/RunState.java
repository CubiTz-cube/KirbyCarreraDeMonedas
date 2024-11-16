package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.variables.PlayerControl;
import src.world.player.Player;

public class RunState extends CanMoveState{
    public RunState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.RUN);
        player.speed =  Player.RUN_SPEED;
        player.maxSpeed =  Player.RUN_MAX_SPEED;
        player.getBody().applyLinearImpulse(player.isFlipX() ? -2 : 2, 0, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        if (Gdx.input.isKeyJustPressed(PlayerControl.ACTION) && player.enemyAbsorded == null){
            player.setState(Player.StateType.ABSORB);
        }

        if (Gdx.input.isKeyPressed(PlayerControl.DOWN)){
            player.setState(Player.StateType.DOWN);
        }
        if (Gdx.input.isKeyJustPressed(PlayerControl.JUMP)){
            player.setState(Player.StateType.JUMP);
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0 && !Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            player.setState(Player.StateType.IDLE);
        }
        if (velocity.y < -1){
            player.setState(Player.StateType.FALL);
        }
    }

    @Override
    public void end() {

    }
}
