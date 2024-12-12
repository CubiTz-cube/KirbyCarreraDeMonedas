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

    public static TextureRegion[] cutVertical(Texture sheet, Integer amount){
        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth(),
            sheet.getHeight() / amount);
        TextureRegion[] frames = new TextureRegion[amount];
        for (int i = 0; i < amount; i++) {
            frames[i] = tmp[i][0];
        }
        return frames;
    }

    public static TextureRegion[] cutSheet(Texture sheet, Integer rows, Integer columns){
        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / columns,
            sheet.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[rows * columns];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return frames;
    }
}
