package src.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class ColorField extends Table {
    private final TextField colorR;
    private final TextField colorG;
    private final TextField colorB;
    private Float opacity;
    private final Color color;

    public ColorField(Skin skin) {
        colorR = new TextField("0.0",skin);
        colorG = new TextField("0.0",skin);
        colorB = new TextField("0.0",skin);
        opacity = 1f;
        color = new Color();

        add(colorR).width(50);
        add(colorG).width(50);
        add(colorB).width(50);
    }

    public boolean addListener(EventListener listener){
        colorR.addListener(listener);
        colorG.addListener(listener);
        colorB.addListener(listener);
        return false;
    }

    @Override
    public Color getColor() {
        try {
            float r = Float.parseFloat(colorR.getText());
            float g = Float.parseFloat(colorG.getText());
            float b = Float.parseFloat(colorB.getText());
            color.set(r,g,b,opacity);
        }catch (NumberFormatException e){
            color.set(1,1,1,opacity);
        }
        return color;
    }

    public void setOpacity(Float opacity) {
        this.opacity = opacity;
    }
}
