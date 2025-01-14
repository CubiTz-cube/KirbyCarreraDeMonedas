package src.utils.indicators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static src.utils.constants.Constants.PIXELS_IN_METER;

public class BorderIndicator extends Actor{
    private final Sprite sprite;
    private final Vector2 targetPosition;
    private final Vector2 centerPosition;

    public BorderIndicator(Texture texture, Vector2 targetPosition){
        sprite = new Sprite(texture);
        sprite.setSize(PIXELS_IN_METER, PIXELS_IN_METER);
        this.targetPosition = targetPosition.scl(PIXELS_IN_METER);
        centerPosition = new Vector2(0, 0);
    }

    /**
     * Cambia la posición del target
     * @param targetPosition vector de posición en metros
     */
    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition.set(targetPosition);
    }

    /**
     * Cambia la posición del centro desde donde se indica la dirección
     * @param centerPosition vector de posición en metros
     */
    public void setCenterPosition(Vector2 centerPosition) {
        this.centerPosition.set(centerPosition.scl(PIXELS_IN_METER));
    }

    @Override
    public void act(float delta) {
        Vector2 direction = new Vector2(targetPosition.scl(PIXELS_IN_METER)).sub(centerPosition).nor();
        float distanceScreen = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 2f - getWidth()/2;

        float distance = centerPosition.dst(targetPosition);
        float scaleFactor = 0.6f + 1.2f * (1.0f - Math.min(distance / 4000, 1.0f));

        sprite.setSize(PIXELS_IN_METER * scaleFactor, PIXELS_IN_METER * scaleFactor);

        if (distance < distanceScreen) sprite.setAlpha(0);
        else sprite.setAlpha(1.0f);

        float cameraZoom = 1280.0f / Gdx.graphics.getWidth();
        if (cameraZoom > 1.3f) cameraZoom = 1.3f;

        distanceScreen *= cameraZoom * 0.9f;

        float posX = centerPosition.x + direction.x * distanceScreen - getWidth() / 2f;
        float posY = centerPosition.y + direction.y * distanceScreen - getHeight() / 2f;

        setPosition(posX, posY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setOriginCenter();
        sprite.setRotation(getRotation());
        sprite.draw(batch);
    }
}


