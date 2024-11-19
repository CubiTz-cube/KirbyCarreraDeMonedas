package src.world.entities.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.animation.SheetCutter;
import src.world.entities.Entity;

public abstract class PlayerAnimations extends Entity {
    public enum AnimationType {
        IDLE,
        WALK,
        JUMP,
        FALL,
        FALLSIMPLE,
        DOWN,
        RUN,
        DASH,
        FLY,
        FLYIN,
        FLYUP,
        FLYEND,
        ABSORB,
        DAMAGE,
        CONSUME,
        SLEEP,
        ABSORBIDLE,
        ABSORBWALK,
        ABSORBRUN,
    }
    private PlayerAnimations.AnimationType currentAnimationType;
    protected final Animation<TextureRegion> walkAnimation;
    protected final Animation<TextureRegion> idleAnimation;
    protected final Animation<TextureRegion> jumpAnimation;
    protected final Animation<TextureRegion> fallAnimation;
    protected final Animation<TextureRegion> fallSimpleAnimation;
    protected final Animation<TextureRegion> downAnimation;
    protected final Animation<TextureRegion> runAnimation;
    protected final Animation<TextureRegion> dashAnimation;
    protected final Animation<TextureRegion> flyAnimation;
    protected final Animation<TextureRegion> inFlyAnimation;
    protected final Animation<TextureRegion> upFlyAnimation;
    protected final Animation<TextureRegion> flyEndAnimation;
    protected final Animation<TextureRegion> absorbAnimation;
    protected final Animation<TextureRegion> damageAnimation;
    protected final Animation<TextureRegion> sleepAnimation;
    protected final Animation<TextureRegion> consumeAnimation;

    protected final Animation<TextureRegion> absorbIdleAnimation;
    protected final Animation<TextureRegion> absorbWalkAnimation;
    protected final Animation<TextureRegion> absorbRunAnimation;

    public PlayerAnimations(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager, id);

        walkAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyWalk.png"), 10));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        idleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyIdle.png"), 31));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        downAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDown.png"), 31));
        downAnimation.setPlayMode(Animation.PlayMode.LOOP);

        jumpAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyJump.png"), 1));

        fallAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFall.png"), 26));

        fallSimpleAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFallSimple.png"), 20));

        runAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyRun.png"), 8));
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);

        dashAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDash.png"), 2));

        flyAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFly.png"), 5));

        inFlyAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyInFly.png"), 2));
        inFlyAnimation.setPlayMode(Animation.PlayMode.LOOP);

        upFlyAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyUpFly.png"), 6));

        flyEndAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFlyEnd.png"), 2));

        absorbAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyAbsorb.png"), 16));

        damageAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDamage.png"), 9));

        consumeAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyConsume.png"), 6));

        sleepAnimation = new Animation<>(0.15f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/sleep/sleep.png"), 33));

        absorbIdleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbIdle.png"), 31));
        absorbIdleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbWalkAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbWalk.png"), 16));
        absorbWalkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        absorbRunAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/absorb/kirbyAbsorbWalk.png"), 16));
        absorbRunAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setAnimation(AnimationType.IDLE);
    }

    public void setAnimation(AnimationType animationType){
        currentAnimationType = animationType;
        switch (animationType){
            case IDLE -> setCurrentAnimation(idleAnimation);
            case WALK -> setCurrentAnimation(walkAnimation);
            case JUMP -> setCurrentAnimation(jumpAnimation);
            case FALL -> setCurrentAnimation(fallAnimation);
            case FALLSIMPLE -> setCurrentAnimation(fallSimpleAnimation);
            case DOWN -> setCurrentAnimation(downAnimation);
            case RUN -> setCurrentAnimation(runAnimation);
            case DASH -> setCurrentAnimation(dashAnimation);
            case FLY -> setCurrentAnimation(flyAnimation);
            case FLYIN -> setCurrentAnimation(inFlyAnimation);
            case FLYUP -> setCurrentAnimation(upFlyAnimation);
            case FLYEND -> setCurrentAnimation(flyEndAnimation);
            case ABSORB -> setCurrentAnimation(absorbAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
            case CONSUME -> setCurrentAnimation(consumeAnimation);
            case SLEEP -> setCurrentAnimation(sleepAnimation);
            case ABSORBIDLE -> setCurrentAnimation(absorbIdleAnimation);
            case ABSORBWALK -> setCurrentAnimation(absorbWalkAnimation);
            case ABSORBRUN -> setCurrentAnimation(absorbRunAnimation);
        }
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }
}
