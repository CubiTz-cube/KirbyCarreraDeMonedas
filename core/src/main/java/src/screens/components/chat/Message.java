package src.screens.components.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class Message extends Label {
    private Integer maxTime;
    private Integer opaqueTime;

    private Float timeDespawn;

    public Message(String text, Label.LabelStyle style, Integer maxTime, Integer opaqueTime) {
        super(text, style);
        this.maxTime = maxTime;
        this.opaqueTime = opaqueTime;
        timeDespawn = 0f;
        setAlignment(Align.topLeft);
    }

    public Message(String text, Label.LabelStyle style) {
        this(text, style, 10, 7);
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
