package src.screens.components;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import src.utils.animation.SheetCutter;
import src.world.entities.player.powers.PowerUp;

public class PowerView extends SpriteAsActor{
    public enum PowerType {
        NORMAL,
        WHEEL,
        BOMB,
        SLEEP,
        SWORD,
    }

    private final TextureRegion[] icons;

    public PowerView(AssetManager assetManager) {
        super(assetManager.get("logo.png", Texture.class));
        icons = SheetCutter.cutHorizontal(assetManager.get("ui/icons/powerIcons.png"), 5);
    }

    public void setPower(PowerUp.Type type){
        switch (type){
            case NONE -> setTextureRegion(icons[PowerType.NORMAL.ordinal()]);
            case BOMB ->  setTextureRegion(icons[PowerType.BOMB.ordinal()]);
            case SLEEP -> setTextureRegion(icons[PowerType.SLEEP.ordinal()]);
            case SWORD -> setTextureRegion(icons[PowerType.SWORD.ordinal()]);
            case WHEEL -> setTextureRegion(icons[PowerType.WHEEL.ordinal()]);
        }
    }
}
