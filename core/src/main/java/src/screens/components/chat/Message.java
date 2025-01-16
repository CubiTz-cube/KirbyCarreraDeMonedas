package src.screens.components.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class Message extends Label {
    private Float timeDespawn;

    public Message(String text, Label.LabelStyle style) {
        super(text, style);
        timeDespawn = 0f;
        setAlignment(Align.topLeft);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int maxTime = 10;
        int opaqueTime = 7;
        timeDespawn += Gdx.graphics.getDeltaTime();

        float alpha = 1f;
        if (timeDespawn > opaqueTime) {
            alpha = Math.max(0, 1 - ((timeDespawn - opaqueTime) / (maxTime - opaqueTime)));
        }

        getColor().a = alpha;
        super.draw(batch, parentAlpha * alpha);

        if (timeDespawn >= maxTime) remove();
    }
}
