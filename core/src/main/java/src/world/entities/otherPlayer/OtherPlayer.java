package src.world.entities.otherPlayer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import src.utils.CollisionFilters;
import src.world.entities.Entity;

import static src.utils.Constants.PIXELS_IN_METER;

public class OtherPlayer extends Entity {
    //protected final StateMachine stateMachine;
    private final String name;
    private final BitmapFont font;
    private final GlyphLayout layout;

    public OtherPlayer(World world, AssetManager assetManager, Rectangle shape, Integer id, String name){
        super(world, id);
        setDebug(true);
        this.name = name;
        sprite = new Sprite(assetManager.get("yoshi.jpg", Texture.class));
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
        this.font = assetManager.get("ui/default.fnt", BitmapFont.class); // Obtén la fuente del AssetManager
        this.layout = new GlyphLayout();

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData("otherPlayer");
        box.dispose();
        body.setFixedRotation(true);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_OTHER_PLAYER;
        filter.maskBits = CollisionFilters.MASK_OTHER_PLAYER;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setSpritePosModification(0f, getHeight()/4);

        //stateMachine = new StateMachine();
        //stateMachine.setState(idleState);
    }

    @Override
    public void act(float delta) {
        //stateMachine.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, name + " " + getId());
        font.draw(batch, layout, getX() + layout.width / 2, getY() + sprite.getHeight() + layout.height);
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
