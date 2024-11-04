package src.world.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.world.entities.Entity;
import src.world.player.Player;

import static src.utils.Constants.PIXELS_IN_METER;

public class BasicEnemy extends Enemy {
    private final BitmapFont font;
    private final GlyphLayout layout;

    public BasicEnemy(World world, AssetManager assetManager, Rectangle shape, Integer id, Float crono) {
        super(world, id, crono);
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

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_NO_COLISION_PLAYER;
        filter.maskBits = CollisionFilters.MASK_PLAYER;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 velocity = body.getLinearVelocity();

        if (actCrono < 3 && velocity.x > -3) {
            body.applyForce(-5, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
            setFlipX(true);
        }else if(actCrono < 6 && velocity.x < 3){
            body.applyForce(5, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
            //body.setLinearVelocity(3,body.getLinearVelocity().y);
            setFlipX(false);
        }else if (actCrono > 9){
            actCrono = 0f;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, "ID " + getId() + " TIME " + actCrono);
        font.draw(batch, layout, getX() + layout.width / 2, getY() + sprite.getHeight() + layout.height);
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
