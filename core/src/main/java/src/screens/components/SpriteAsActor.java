package src.screens.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SpriteAsActor extends Actor {
    private final Sprite sprite;
    private Float width;
    private Float height;

    public SpriteAsActor(Texture texture) {
        sprite = new Sprite(texture);
        setSize(sprite.getWidth(), sprite.getHeight());
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void setColor(Color color) {
        sprite.setColor(color);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        sprite.setColor(r, g, b, a);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
        sprite.draw(batch);
    }

    public void setTexture(Texture texture) {
        sprite.setTexture(texture);
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        sprite.setRegion(textureRegion);
        sprite.setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        width = (float)textureRegion.getRegionWidth();
        height = (float)textureRegion.getRegionHeight();
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        sprite.setScale(scaleXY);
    }
}
