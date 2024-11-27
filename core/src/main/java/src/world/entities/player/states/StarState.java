package src.world.entities.player.states;

import com.badlogic.gdx.math.Vector2;
import src.world.entities.Entity;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class StarState extends StatePlayer{
    public StarState(Player player) {
        super(player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.FLYEND);
        player.game.addEntityWithForce(Entity.Type.STAR,
            player.getBody().getPosition().add(player.isFlipX() ? -1.2f : 1.2f,0),
            new Vector2((player.isFlipX() ? -3f : 3f) + player.getBody().getLinearVelocity().x,0));
        player.enemyAbsorded = null;
    }

    @Override
    public void update(Float delta) {
        if (player.isAnimationFinish()) player.setCurrentState(PlayerCommon.StateType.IDLE);
    }

    @Override
    public void end() {

    }
}
