package src.world.entities.player.powers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public class PowerSleep extends PowerUp {
    private final Animation<TextureRegion> powerSleepAnimation ;

    public PowerSleep(PlayerCommon player) {
        super(player);

        powerSleepAnimation = new Animation<>(0.15f,
            SheetCutter.cutHorizontal(player.assetManager.get("world/entities/kirby/sleep/sleep.png"), 28));
    }

    @Override
    public void start() {
        super.start();
        player.stunTime = 5;
        player.setCurrentState(Player.StateType.STUN);
        player.setAnimation(Player.AnimationType.SLEEP);
        if (player instanceof Player mainPlayer) mainPlayer.playSound(Player.SoundType.SLEEP);
    }

    @Override
    public void update(Float delta) {
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
    public Animation<TextureRegion> getAnimation(PlayerCommon.AnimationType type) {
        return null;
    }

    @Override
    public Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type) {
        if (type == PlayerCommon.AnimationType.SLEEP) return powerSleepAnimation;
        return null;
    }

    @Override
    public Sound getSound(Player.SoundType type) {
        return null;
    }
}
