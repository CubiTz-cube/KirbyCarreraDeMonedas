package src.world.entities.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import src.utils.constants.PlayerControl;
import src.world.entities.player.Player;
import src.world.particles.ParticleFactory;

public class RunState extends CanBasicMoveState{
    private Boolean isLeft = false;
    private Float timeInverse = 0f, timeParticle = 0f, timeActivateParticle = 0f;

    public RunState(Player player) {
        super(player);
    }

    private void playRunAnimation(){
        if (player.isEnemyAbsorb()) player.setAnimation(Player.AnimationType.ABSORBRUN);
        else player.setAnimation(Player.AnimationType.RUN);
    }

    @Override
    public void start() {
        playRunAnimation();

        player.speed =  Player.RUN_SPEED;
        player.maxSpeed =  Player.RUN_MAX_SPEED;
        //player.getBody().applyLinearImpulse(player.isFlipX() ? -2 : 2, 0, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);

        isLeft = player.isFlipX();
        timeActivateParticle = 0.3f;
    }

    @Override
    public void update(Float delta) {
        super.update(delta);

        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x == 0 && !Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            player.setCurrentState(Player.StateType.IDLE);
        }

        if (timeActivateParticle > 0){
            timeActivateParticle -= delta;
            timeParticle += delta;
            if (timeParticle > 0.1f){
                float X;
                if (player.getCurrentAnimationType() == Player.AnimationType.CHANGERUN) X = player.getBody().getPosition().x + (isLeft ? -0.8f : 0.8f);
                else X = player.getBody().getPosition().x + (isLeft ? 0.8f : -0.8f);
                player.game.addParticle(ParticleFactory.Type.CLOUD, new Vector2(X, player.getBody().getPosition().y-0.1f));
                timeParticle = 0f;
            }
        }

        if (player.getCurrentAnimationType() == Player.AnimationType.CHANGERUN) timeInverse += delta;
        if (timeInverse > 0.3f){
            playRunAnimation();
            timeInverse = 0f;
        }

        if (!player.isEnemyAbsorb()) return;

        if (Gdx.input.isKeyJustPressed(PlayerControl.LEFT) && !isLeft){
            player.setAnimation(Player.AnimationType.CHANGERUN);
            timeActivateParticle = 0.3f;
            isLeft = true;
        }else if (Gdx.input.isKeyJustPressed(PlayerControl.RIGHT) && isLeft){
            player.setAnimation(Player.AnimationType.CHANGERUN);
            timeActivateParticle = 0.3f;
            isLeft = false;
        }
    }

    @Override
    public void end() {

    }
}
