package src.world.entities.player.powers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerWheel extends PowerUp{
    private final Animation<TextureRegion> powerSleepAnimation ;

    public PowerWheel(PlayerCommon player) {
        super(player);

        powerSleepAnimation = new Animation<>(0.15f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sleep/sleep.png"), 28));
    }

    @Override
    public void start() {
        super.start();
        player.maxSpeed = Player.RUN_MAX_SPEED + 3;
        player.speed = Player.RUN_SPEED + 3;
        player.brakeForce = Player.DEFAULT_BRAKE_FORCE - 180;
    }

    @Override
    public void end(){
        super.end();
        player.maxSpeed = Player.RUN_MAX_SPEED;
        player.speed = Player.RUN_SPEED;
        player.brakeForce = Player.DEFAULT_BRAKE_FORCE;
    }

    @Override
    public void actionIdle() {

    }

    @Override
    public void actionMove() {
    }

    @Override
    public void actionAir() {
    }

    @Override
    public void update() {
    }

    @Override
    public Animation<TextureRegion> getAnimation(PlayerCommon.AnimationType type) {
        return switch (type){
            case WALK -> powerSleepAnimation;
            case RUN -> powerSleepAnimation;
            default -> null;
        };
    }

    @Override
    public Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type) {
        return null;
    }
}
