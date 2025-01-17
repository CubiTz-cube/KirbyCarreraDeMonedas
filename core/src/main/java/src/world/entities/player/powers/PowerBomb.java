package src.world.entities.player.powers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.Entity;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerBomb extends PowerUp{
    private Float cooldown = 0f;

    private Animation<TextureRegion> idleBombAnimation;
    private Animation<TextureRegion> walkBombAnimation;
    private Animation<TextureRegion> jumpBombAnimation;
    private Animation<TextureRegion> fallBombAnimation;
    private Animation<TextureRegion> fallSimpleBombAnimation;
    private Animation<TextureRegion> downBombAnimation;
    private Animation<TextureRegion> runBombAnimation;
    private Animation<TextureRegion> changeRunBombAnimation;
    private Animation<TextureRegion> dashBombAnimation;
    private Animation<TextureRegion> flyBombAnimation;
    private Animation<TextureRegion> flyInBombAnimation;
    private Animation<TextureRegion> upFlyBombAnimation;
    private Animation<TextureRegion> flyEndBombAnimation;

    public PowerBomb(PlayerCommon player) {
        super(player);
        initAniations();
    }

    @Override
    public void start() {
        super.start();
    }

    private void initAniations(){
        idleBombAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyIdleBomb.png"), 1));

        walkBombAnimation = new Animation<>(0.11f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyWalkBomb.png"), 10));
        walkBombAnimation.setPlayMode(Animation.PlayMode.LOOP);

        downBombAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyDownBomb.png"), 1));

        jumpBombAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyJumpBomb.png"), 1));

        fallBombAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyFallBomb.png"), 26));

        fallSimpleBombAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyFallSimpleBomb.png"), 20));

        runBombAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyRunBomb.png"), 8));
        runBombAnimation.setPlayMode(Animation.PlayMode.LOOP);

        changeRunBombAnimation = new Animation<>(1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyChangeRunBomb.png"), 1));

        dashBombAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyDashBomb.png"), 2));

        flyBombAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyFlyBomb.png"), 5));

        flyInBombAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyInFlyBomb.png"), 2));
        flyInBombAnimation.setPlayMode(Animation.PlayMode.LOOP);

        upFlyBombAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyUpFlyBomb.png"), 6));

        flyEndBombAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/bomb/kirbyFlyEndBomb.png"), 2));
    }

    @Override
    public void actionIdle() {
        if (cooldown > 0) return;
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.BOMB, 0f,0f);
        cooldown = 1f;
    }

    @Override
    public void actionMove() {
        if (cooldown > 0) return;
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.BOMB, 2f,3f);
        cooldown = 1f;
    }

    @Override
    public void actionAir() {
        if (cooldown > 0) return;
        Player mainPlayer = (Player) player;
        mainPlayer.throwEntity(Entity.Type.BOMB, 1f,8f);
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
        return null;
    }

    @Override
    public Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type) {
        return switch (type){
            case IDLE -> idleBombAnimation;
            case DOWN -> downBombAnimation;
            case FLYUP -> upFlyBombAnimation;
            case WALK -> walkBombAnimation;
            case JUMP -> jumpBombAnimation;
            case FALL -> fallBombAnimation;
            case FALLSIMPLE -> fallSimpleBombAnimation;
            case RUN -> runBombAnimation;
            case CHANGERUN -> changeRunBombAnimation;
            case DASH -> dashBombAnimation;
            case FLY -> flyBombAnimation;
            case FLYIN -> flyInBombAnimation;
            case FLYEND -> flyEndBombAnimation;
            default -> null;
        };
    }

    @Override
    public Sound getSound(Player.SoundType type) {
        return null;
    }
}
