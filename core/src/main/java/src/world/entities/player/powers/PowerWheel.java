package src.world.entities.player.powers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerWheel extends PowerUp{
    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> dashAnimation;
    private final Animation<TextureRegion> idleAnimation;

    public PowerWheel(PlayerCommon player) {
        super(player);

        runAnimation = new Animation<>(0.07f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyRunWheel.png"), 4));
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);

        dashAnimation  = new Animation<>(0.07f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyDashWheel.png"), 5));
        dashAnimation.setPlayMode(Animation.PlayMode.LOOP);

        idleAnimation  = new Animation<>(1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyIdleWheel.png"), 1));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void start() {
        super.start();
        player.brakeForce = Player.DEFAULT_BRAKE_FORCE - 180;
    }

    @Override
    public void end() {
        super.end();
        player.brakeForce = Player.DEFAULT_BRAKE_FORCE;
    }

    @Override
    public void actionIdle() {
        player.setCurrentState(Player.StateType.DASH);
    }

    @Override
    public void actionMove() {
        actionIdle();
    }

    @Override
    public void actionAir() {
        actionIdle();
    }

    @Override
    public void update(Float delta) {
        if (player.getCurrentStateType() == PlayerCommon.StateType.RUN){
            player.maxSpeed = Player.RUN_MAX_SPEED + 3;
            player.speed = Player.RUN_SPEED + 3;
        }
    }

    @Override
    public Animation<TextureRegion> getAnimation(PlayerCommon.AnimationType type) {
        return switch (type){
            case DASH -> dashAnimation;
            case RUN, FALL, FALLSIMPLE, JUMP -> runAnimation;
            default -> null;
        };
    }

    @Override
    public Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type) {
        return switch (type){
            case IDLE -> idleAnimation;
            default -> null;
        };
    }
}
