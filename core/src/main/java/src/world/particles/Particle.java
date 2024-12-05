package src.world.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.screens.GameScreen;

import java.util.Random;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class Particle extends Actor {
    private final Sprite sprite;
    private Float animateTime, actualTime;
    private Animation<TextureRegion> animation;
    private final GameScreen game;
    private Boolean isFliX;

    private final Float maxTime;

    /**
     *
     * @param assetManager gestor de recursos
     * @param game pantalla en la que se encuentra la particula
     * @param shape posicion y tamaño de la particula en metros del world
     */
    public Particle(AssetManager assetManager, Rectangle shape, GameScreen game, Float maxTime) {
        this.game = game;
        this.maxTime = maxTime;
        animateTime = 0f;//new Random().nextFloat(1);
        actualTime = 0f;
        sprite = new Sprite();
        isFliX = false;
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
        setBounds(shape.x * PIXELS_IN_METER, shape.y * PIXELS_IN_METER, shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public void setFliX(Boolean fliX) {
        isFliX = fliX;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setRegion(animation.getKeyFrame(animateTime, false));
        sprite.setPosition(getX(), getY());
        sprite.setOriginCenter();
        sprite.setFlip(isFliX, false);
        sprite.draw(batch);

        animateTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void act(float delta) {
        actualTime += delta;
        if (actualTime >= maxTime) {
            game.removeActor(this);
        }
    }
}
