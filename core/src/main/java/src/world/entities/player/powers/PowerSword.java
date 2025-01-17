package src.world.entities.player.powers;

import com.badlogic.gdx.audio.Sound;
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
    private Animation<TextureRegion> walkSwordAnimation;
    private Animation<TextureRegion> jumpSwordAnimation;
    private Animation<TextureRegion> fallSwordAnimation;
    private Animation<TextureRegion> fallSimpleSwordAnimation;
    private Animation<TextureRegion> downSwordAnimation;
    private Animation<TextureRegion> runSwordAnimation;
    private Animation<TextureRegion> changeRunSwordAnimation;
    private Animation<TextureRegion> dashSwordAnimation;
    private Animation<TextureRegion> flySwordAnimation;
    private Animation<TextureRegion> flyInSwordAnimation;
    private Animation<TextureRegion> upFlySwordAnimation;
    private Animation<TextureRegion> flyEndSwordAnimation;

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

        walkAnimation = new Animation<>(0.11f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/hold/kirbyWalkHold.png"), 10));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        //Sword

        idleSwordAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyIdleSword.png"), 1));

        walkSwordAnimation = new Animation<>(0.11f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyWalkSword.png"), 10));
        walkSwordAnimation.setPlayMode(Animation.PlayMode.LOOP);

        downSwordAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyDownSword.png"), 1));

        jumpSwordAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyJumpSword.png"), 1));

        fallSwordAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyFallSword.png"), 26));

        fallSimpleSwordAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyFallSimpleSword.png"), 20));

        runSwordAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyRunSword.png"), 8));
        runSwordAnimation.setPlayMode(Animation.PlayMode.LOOP);

        changeRunSwordAnimation = new Animation<>(1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyChangeRunSword.png"), 1));

        dashSwordAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyDashSword.png"), 2));

        flySwordAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyFlySword.png"), 5));

        flyInSwordAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyInFlySword.png"), 2));
        flyInSwordAnimation.setPlayMode(Animation.PlayMode.LOOP);

        upFlySwordAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyUpFlySword.png"), 6));

        flyEndSwordAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sword/kirbyFlyEndSword.png"), 2));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void actionIdle() {
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.SWORDPROPLAYER,5f,0f);
        cooldown = 1f;
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
        cooldown = 1f;
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
            case DOWN -> downSwordAnimation;
            case FLYUP -> upFlySwordAnimation;
            case WALK -> walkSwordAnimation;
            case JUMP -> jumpSwordAnimation;
            case FALL -> fallSwordAnimation;
            case FALLSIMPLE -> fallSimpleSwordAnimation;
            case RUN -> runSwordAnimation;
            case CHANGERUN -> changeRunSwordAnimation;
            case DASH -> dashSwordAnimation;
            case FLY -> flySwordAnimation;
            case FLYIN -> flyInSwordAnimation;
            case FLYEND -> flyEndSwordAnimation;
            default -> null;
        };
    }

    @Override
    public Sound getSound(Player.SoundType type) {
        return null;
    }
}
