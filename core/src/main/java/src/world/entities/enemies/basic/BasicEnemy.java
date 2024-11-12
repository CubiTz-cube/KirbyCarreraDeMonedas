package src.world.entities.enemies.basic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.basic.states.*;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class BasicEnemy extends Enemy {
    private final BitmapFont font;
    private final GlyphLayout layout;

    public BasicEnemy(World world, AssetManager assetManager, Rectangle shape, Integer id) {
        super(world, id);
        type = Type.BASIC;
        sprite = new Sprite(assetManager.get("perro.jpg", Texture.class));
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
        this.font = assetManager.get("ui/default.fnt", BitmapFont.class);
        this.layout = new GlyphLayout();

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/2, shape.height/2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("enemy");
        box.dispose();
        body.setFixedRotation(true);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);

        idleState = new IdleStateBasic(stateMachine, this);
        walkState = new WalkStateBasic(stateMachine, this);
        setState(StateType.IDLE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, "ID " + getId() + " TIME " + getActCrono());
        font.draw(batch, layout, getX() + layout.width / 2, getY() + sprite.getHeight() + layout.height);
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
