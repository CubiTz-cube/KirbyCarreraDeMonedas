package src.net;

import com.badlogic.gdx.graphics.Color;

public class PlayerInfo {
    private final String name;
    private final Color color;

    public PlayerInfo(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Float r, Float g, Float b, Float a) {
        this.color.set(r, g, b, a);
    }
}
