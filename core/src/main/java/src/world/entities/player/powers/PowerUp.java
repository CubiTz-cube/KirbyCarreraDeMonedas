package src.world.entities.player.powers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;

public abstract class PowerUp
{
    public enum Type{ SLEEP, SWORD, BOMB, WHEEL, NONE}
    protected PlayerCommon player;

    public PowerUp(PlayerCommon player)
    {
        this.player = player;
    }

    public void start() {
        player.setAnimation(player.getCurrentAnimationType());
    }

    public void end(){
        player.setSecondCurrentAnimation(null);
    };

    public abstract void actionIdle();

    public abstract void actionMove();

    public abstract void actionAir();

    public abstract void update(Float delta);

    public abstract Animation<TextureRegion> getAnimation(PlayerCommon.AnimationType type);

    public abstract Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type);

    public abstract Sound getSound(Player.SoundType type);
}
