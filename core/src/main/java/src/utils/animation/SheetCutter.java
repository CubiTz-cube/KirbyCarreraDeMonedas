package src.utils.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SheetCutter {
    public static TextureRegion[] cutHorizontal(Texture sheet, Integer amount){
        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / amount,
            sheet.getHeight());
        TextureRegion[] frames = new TextureRegion[amount];
        System.arraycopy(tmp[0], 0, frames, 0, amount);
        return frames;
    }
}
