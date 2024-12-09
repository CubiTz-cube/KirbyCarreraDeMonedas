package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.screens.GameScreen;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;


public class Platform extends Floor {

    private final GameScreen game;
    public Platform(World world, Rectangle shape,GameScreen game) {
        super(world, shape);
        this.game = game;
    }

    @Override
    public void act(float delta) {

        Player player = game.getPlayer();
        boolean playerUnderPlatform = player.getY() < this.getY() + this.getHeight() && player.getY() > this.getY() - 2 *this.getHeight();
        boolean playerDown = player.getCurrentStateType() == PlayerCommon.StateType.DOWN;

        if (!playerUnderPlatform && playerDown) {
            game.threadSecureWorld.addModification(() -> fixture.setSensor(playerDown));
        }

        if (fixture.isSensor()!=playerUnderPlatform){
            game.threadSecureWorld.addModification(() -> fixture.setSensor(playerUnderPlatform));
        }
    }
}

