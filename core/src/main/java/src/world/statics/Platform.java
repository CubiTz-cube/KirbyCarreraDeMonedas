package src.world.statics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.screens.GameScreen;
import src.utils.constants.CollisionFilters;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;


public class Platform extends Floor {
    private final Filter noPlayerCollisionFilter;
    private final Filter playerCollisionFilter;
    private Boolean playerCollision;

    private final GameScreen game;
    public Platform(World world, Rectangle shape,GameScreen game) {
        super(world, shape);
        this.game = game;
        playerCollision = true;

        noPlayerCollisionFilter = new Filter();
        noPlayerCollisionFilter.categoryBits = CollisionFilters.STATIC;
        noPlayerCollisionFilter.maskBits = (short)~CollisionFilters.PLAYER;

        playerCollisionFilter = new Filter();
        playerCollisionFilter.categoryBits = CollisionFilters.STATIC;
    }

    @Override
    public void act(float delta) {

        Player player = game.getPlayer();
        boolean playerOnPlatform = player.getY() > this.getY() + this.getHeight();
        boolean playerDown = player.getCurrentStateType() == PlayerCommon.StateType.DOWN;

        if (playerOnPlatform && playerDown) {
            game.threadSecureWorld.addModification(() -> setPlayerCollision(false));
        }

        if (playerCollision!= playerOnPlatform){
            game.threadSecureWorld.addModification(() -> setPlayerCollision(playerOnPlatform));
        }
    }

    private void setPlayerCollision(Boolean collision){
        if (collision) fixture.setFilterData(playerCollisionFilter);
        else fixture.setFilterData(noPlayerCollisionFilter);
        playerCollision = collision;
        System.out.println("Collision con player: " + collision);
    }
}

