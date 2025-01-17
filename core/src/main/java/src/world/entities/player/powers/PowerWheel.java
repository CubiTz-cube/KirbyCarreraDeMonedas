package src.world.entities.player.powers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerWheel extends PowerUp{
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> dashAnimation;
    private Animation<TextureRegion> idleWheelAnimation;
    private Animation<TextureRegion> walkWheelAnimation;
    private Animation<TextureRegion> downWheelAnimation;
    private Animation<TextureRegion> flyWheelAnimation;
    private Animation<TextureRegion> flyInWheelAnimation;
    private Animation<TextureRegion> upFlyWheelAnimation;
    private Animation<TextureRegion> flyEndWheelAnimation;

    private final Sound dashSound;

    public PowerWheel(PlayerCommon player) {
        super(player);

        initAnimations();

        dashSound = player.assetManager.get("sound/kirby/powers/kirbyWheelDash.wav");
    }

    private void initAnimations(){
        runAnimation = new Animation<>(0.07f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyRunWheel.png"), 4));
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);

        dashAnimation  = new Animation<>(0.07f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyDashWheel.png"), 5));
        dashAnimation.setPlayMode(Animation.PlayMode.LOOP);

        //Wheel

        idleWheelAnimation  = new Animation<>(1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyIdleWheel.png"), 1));

        walkWheelAnimation = new Animation<>(0.11f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyWalkWheel.png"), 10));
        walkWheelAnimation.setPlayMode(Animation.PlayMode.LOOP);

        downWheelAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyDownWheel.png"), 1));

        flyWheelAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyFlyWheel.png"), 5));

        flyInWheelAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyInFlyWheel.png"), 2));
        flyInWheelAnimation.setPlayMode(Animation.PlayMode.LOOP);

        upFlyWheelAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyUpFlyWheel.png"), 6));

        flyEndWheelAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/wheel/kirbyFlyEndWheel.png"), 2));
    }

    @Override
    public void start() {
        super.start();
        player.brakeForce = Player.DEFAULT_BRAKE_FORCE - 180;
        player.dashDamage = 6;
    }

    @Override
    public void end() {
        super.end();
        player.brakeForce = Player.DEFAULT_BRAKE_FORCE;
        player.dashDamage = Player.DEFAULT_DASH_DAMAGE;
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
            case IDLE -> idleWheelAnimation;
            case DOWN -> downWheelAnimation;
            case FLYUP -> upFlyWheelAnimation;
            case WALK -> walkWheelAnimation;
            case FLY -> flyWheelAnimation;
            case FLYIN -> flyInWheelAnimation;
            case FLYEND -> flyEndWheelAnimation;
            default -> null;
        };
    }

    @Override
    public Sound getSound(Player.SoundType type) {
        return switch (type){
            case DASH -> dashSound;
            default -> null;
        };
    }
}
