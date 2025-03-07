package src.screens.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;

public class ColorPickerImage extends Image implements Disposable {
    private final Pixmap pixmap;
    private final Color selectColor;

    public ColorPickerImage(Texture texture) {
        super(texture);
        this.pixmap = textureToPixmap(texture);
        selectColor = new Color();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isInsideCircle(x, y)) return;

                int pixelX = (int) x;
                int pixelY = (int) (getHeight() - y);
                int colorInt = pixmap.getPixel(pixelX, pixelY);
                if (colorInt == 0x00000000) {
                    selectColor.set(Color.WHITE);
                    return;
                }

                selectColor.set(colorInt);
            }
        });
    }

    private boolean isInsideCircle(float x, float y) {
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = pixmap.getWidth() / 2f - 1;

        float dx = x - centerX;
        float dy = y - centerY;

        return (dx * dx + dy * dy) <= (radius * radius);
    }

    public Color getSelectColor() {
        return selectColor;
    }

    private Pixmap textureToPixmap(Texture texture) {
        texture.getTextureData().prepare();
        return texture.getTextureData().consumePixmap();
    }

    @Override
    public void dispose() {
        pixmap.dispose();
    }
}
