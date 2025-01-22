package src.screens.components;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class SliderText extends Table {
    private final Slider slider;
    private final Label label;

    public SliderText(float min, float max, float stepSize, boolean vertical, Skin skin, String labelText) {
        super();
        slider = new Slider(min, max, stepSize, vertical, skin);
        label = new Label(labelText, skin);

        label.setAlignment(Align.center);
        add(label).row();
        add(slider).fillX();
    }

    public void setValue(Float value){
        slider.setValue(value);
    }
    public Float getValue(){
        return slider.getValue();
    }

    @Override
    public boolean addListener(EventListener listener) {
        return slider.addListener(listener);
    }

    public Slider getSlider() {
        return slider;
    }

    public Label getLabel() {
        return label;
    }
}
