package src.world.entities.player.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;
import src.world.particles.ParticleFactory;

public class DashState extends StatePlayer{
    private Float timeParticle = 0f;

    public DashState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        timeParticle = 0f;
        player.setAnimation(Player.AnimationType.DASH);
        player.playSound(Player.SoundType.DASH);
        player.getBody().applyLinearImpulse(
            player.getSprite().isFlipX() ? -Player.DASH_IMPULSE : Player.DASH_IMPULSE,
            0, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
    }

    @Override
    public void update(Float delta) {
        timeParticle += delta;
        float velocityX = player.getBody().getLinearVelocity().x;
        if (velocityX != 0) {
            player.getBody().applyForce(-velocityX * player.brakeForce * delta, 0, player.getBody().getWorldCenter().x, player.getBody().getWorldCenter().y, true);
            if (timeParticle > 0.1f){
                float X = player.getBody().getPosition().x + (player.isFlipX() ? 0.8f : -0.8f);
                player.game.addParticle(ParticleFactory.Type.CLOUD, new Vector2(X, player.getBody().getPosition().y-0.1f));
                timeParticle = 0f;
            }
        }else {
            player.setCurrentState(Player.StateType.RUN);
        }
    }

    @Override
    public void end() {

    }
}
