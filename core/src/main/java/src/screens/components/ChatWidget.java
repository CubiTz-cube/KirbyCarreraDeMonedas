package src.screens.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
