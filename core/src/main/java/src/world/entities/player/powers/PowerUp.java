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

    /**
     * Cambia la animacion por colocarse en la capa BASE por la que devuelvas. Si se devuelve NULL se mantiene la original
     * @param type El tipo de animacion que se esta por colocar
     * @return Animation<TextureRegion> Animacion sobrepuesta
     */
    public abstract Animation<TextureRegion> getAnimation(PlayerCommon.AnimationType type);

    /**
     * Cambia la animacion por colocarse en la capa SUPERIOR por la que devuelvas. Si se devuelve NULL se mantiene la original
     * @param type El tipo de animacion que se esta por colocar
     * @return Animation<TextureRegion> Animacion sobrepuesta
     */
    public abstract Animation<TextureRegion> getSecondAnimation(PlayerCommon.AnimationType type);

    /**
     * Cambia el sonido que se esta por reproducir devuelvas. Si se devuelve NULL se mantiene el original
     * @param type El tipo de sonido que se esta por colocar
     * @return Sound Sonido sobrepuesto
     */
    public abstract Sound getSound(Player.SoundType type);
}
