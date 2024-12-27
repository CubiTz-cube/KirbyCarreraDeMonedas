package src.world.entities.player.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.Entity;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerSword extends PowerUp {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> upFlyAnimation;
    private Animation<TextureRegion> walkAnimation;

    private Animation<TextureRegion> idleSwordAnimation;

    private Float cooldown = 0f;

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

        idleSwordAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyIdleSword.png"), 1));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void actionIdle() {
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.SWORDPROPLAYER,5f,0f);
        cooldown = 0.2f;
    }

    @Override
    public void actionMove() {
        if (cooldown > 0) return;
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.SWORDRUNPROPLAYER,25f,0f);
        cooldown = 1f;
    }

    @Override
    public void actionAir() {
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.SWORDPROPLAYER,5f, Player.ThrowDirection.LEFT);
        mainPlayer.throwEntity(Entity.Type.SWORDPROPLAYER,5f, Player.ThrowDirection.RIGHT);
        cooldown = 0.5f;
    }

    @Override
    public void update(Float delta) {
        if (cooldown > 0){
            cooldown -= delta;
        }
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
        return switch (type){
            case IDLE -> idleSwordAnimation;
            case DOWN -> null;
            case FLYUP -> null;
            case WALK -> null;
            default -> null;
        };
    }
}
