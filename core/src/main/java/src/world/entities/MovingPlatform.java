package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
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

    public MovingPlatform(World world, Rectangle shape, AssetManager assetManager, Integer id, Vector2 impulso) {
        super(world, shape, assetManager,id);
        sprite = new Sprite(assetManager.get("world/entities/breakBlock.png", Texture.class));

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(shape.x, shape.y);
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.x / 2, shape.y / 2);

        fixture = body.createFixture(box, 0.0f);
        fixture.setUserData(this);
        box.dispose();

        this.velocidad = impulso;
        body.setLinearVelocity(velocidad);
        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setPosition(shape.x * PIXELS_IN_METER, shape.y * PIXELS_IN_METER);
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
