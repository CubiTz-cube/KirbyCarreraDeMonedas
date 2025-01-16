package src.screens.components.chat;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class Message extends Label {
    public Message(String text, Label.LabelStyle style) {
        super(text, style);
        setAlignment(Align.topLeft);
    }
}
