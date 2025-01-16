package src.screens.components.chat;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class Chat extends Table {
    private final Label.LabelStyle labelStyle;
    private Integer numMessages;
    private Float timeDespawnMessage;

    /**
     * Crea un chat dentro de una tabla
     * @param labelStyle style de los mensajes de la tabla
     */
    public Chat(Label.LabelStyle labelStyle){
        this.labelStyle = labelStyle;
        numMessages = 0;
        timeDespawnMessage = 0f;
        setFillParent(true);
        bottom();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (timeDespawnMessage > 0) timeDespawnMessage -= delta;
        if (timeDespawnMessage <= 0 && numMessages > 0){
            System.out.println("Despawn message");
            removeActorAt(0, true);
            numMessages--;
            timeDespawnMessage = 3f;
        }
    }

    public void addMessage(String message){
        Message newMessage = new Message(message, labelStyle);
        add(newMessage).expandX().fillX().row();
        numMessages++;
        timeDespawnMessage = 3f;

        if (numMessages > 3){
            removeActorAt(0, true);
            numMessages = 3;
        }
    }
}
