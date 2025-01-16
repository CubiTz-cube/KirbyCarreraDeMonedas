package src.screens.components.chat;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class Chat extends Table {
    private final Label.LabelStyle labelStyle;

    /**
     * Crea un chat dentro de una tabla
     * @param labelStyle style de los mensajes de la tabla
     */
    public Chat(Label.LabelStyle labelStyle){
        this.labelStyle = labelStyle;
        setFillParent(true);
        pad(20);
        bottom();
    }

    public void addMessage(String message){
        Message newMessage = new Message(message, labelStyle);
        add(newMessage).expandX().fillX().left().row();
    }
}
