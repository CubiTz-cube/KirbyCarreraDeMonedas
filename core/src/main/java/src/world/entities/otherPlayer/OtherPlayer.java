package src.world.entities.otherPlayer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.world.entities.Entity;

import static src.utils.Constants.PIXELS_IN_METER;

public class OtherPlayer extends Entity {
    private final Sprite sprite;
    private final World world;

    private final Fixture fixture;

    //protected final StateMachine stateMachine;

    public OtherPlayer(World world, Texture texture, Rectangle shape, Integer id){
        super(id);
        this.world = world;
        this.sprite = new Sprite(texture);

        BodyDef def = new BodyDef();
        def.position.set(shape.x, shape.y);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width, shape.height);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("otherPlayer");
        box.dispose();

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_OTHER_PLAYER;
        filter.maskBits = CollisionFilters.MASK_OTHER_PLAYER;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

        //stateMachine = new StateMachine();
        //stateMachine.setState(idleState);
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(
            body.getPosition().x * PIXELS_IN_METER,
            body.getPosition().y * PIXELS_IN_METER
        );
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
        sprite.setOriginCenter();
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        //stateMachine.update(delta);
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
