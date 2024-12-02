package src.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class ChatWidget extends Table {

    /**
     * Crea un chat dentro de una tabla
     * @param skin skin de la tabla
     */
    public ChatWidget(Skin skin){
        setSkin(skin);
        setFillParent(true);
        bottom();
    }

    public void addMessage(String message){
        Label newLabel = new Label(message, getSkin());
        newLabel.setAlignment(Align.topLeft);
        newLabel.setFontScale(2);
        add(newLabel).expandX().fillX().row();
    }
}
