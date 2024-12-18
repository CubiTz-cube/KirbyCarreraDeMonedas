package src.world.entities.player.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.player.PlayerCommon;

public class PowerSword extends PowerUp {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> upFlyAnimation;
    private Animation<TextureRegion> walkAnimation;

    public PowerSword(PlayerCommon player) {
        super(player);
        initAnimations();
    }

    public void initAnimations(){
        idleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/hold/kirbyIdleHold.png"), 31));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        downAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/hold/kirbyDownHold.png"), 31));
        downAnimation.setPlayMode(Animation.PlayMode.LOOP);

        upFlyAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/hold/kirbyUpFlyHold.png"), 6));

        walkAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/hold/kirbyWalkHold.png"), 10));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void start() {
        super.start();
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
            case IDLE -> idleAnimation;
            case DOWN -> downAnimation;
            case FLYUP -> upFlyAnimation;
            case WALK -> walkAnimation;
            default -> null;
        };
    }

    @Override
    public Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type) {
        return null;
    }
}
