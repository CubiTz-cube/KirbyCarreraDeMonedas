package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.animation.SheetCutter;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class MovingPlatform extends Entity {
    private Vector2 velocidad;

    public MovingPlatform(World world, AssetManager assetManager, Rectangle shape, Integer id, Vector2 impulso) {
        super(world, assetManager, shape,id);
        sprite = new Sprite();

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(shape.x, shape.y);
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.x / 2, shape.y / 2);

        fixture = body.createFixture(box, 0.0f);
        fixture.setUserData("movingPlatform");
        box.dispose();

        this.velocidad = impulso;
        body.setLinearVelocity(velocidad);
        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setPosition(shape.x * PIXELS_IN_METER, shape.y * PIXELS_IN_METER);

        Animation<TextureRegion> idleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyIdle.png"), 31));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        setCurrentAnimation(idleAnimation);
    }

    public void update(float delta) {
        Vector2 posicion = body.getPosition();

        /*if () {

            if () {
            }
        } else {
            body.setLinearVelocity(velocidad.scl(-1)); // Mover en la dirección contraria
            if () {
            }
        }*/
    }

    public Body getBody() {
        return body;
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
