package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.variables.PlayerControl;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerAnimations;

public class RunState extends CanBasicMoveState{
    private Boolean isLeft = false;
    private Float time = 0f;

    public RunState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        if (player.enemyAbsorded == null) player.setAnimation(Player.AnimationType.RUN);
        else player.setAnimation(Player.AnimationType.ABSORBRUN);

        player.speed =  Player.RUN_SPEED;
        player.maxSpeed =  Player.RUN_MAX_SPEED;
        player.getBody().applyLinearImpulse(player.isFlipX() ? -2 : 2, 0, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);

        isLeft = player.isFlipX();
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0 && !Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            player.setState(Player.StateType.IDLE);
        }

        if (player.getCurrentAnimationType() == Player.AnimationType.CHANGERUN) time += delta;
        if (time > 0.3f){
            player.setAnimation(Player.AnimationType.RUN);
            time = 0f;
        }

        if (Gdx.input.isKeyJustPressed(PlayerControl.LEFT) && !isLeft){
            player.setAnimation(Player.AnimationType.CHANGERUN);
            isLeft = true;
        }else if (Gdx.input.isKeyJustPressed(PlayerControl.RIGHT) && isLeft){
            player.setAnimation(Player.AnimationType.CHANGERUN);
            isLeft = false;
        }
    }

    @Override
    public void end() {

    }
}
