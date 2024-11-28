package src.world.entities.otherPlayer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.world.entities.NoAutoPacketEntity;
import src.world.entities.player.PlayerCommon;

public class OtherPlayer extends PlayerCommon implements NoAutoPacketEntity {
    private final String name;
    private final BitmapFont font;
    private final GlyphLayout layout;

    public OtherPlayer(World world, AssetManager assetManager, Rectangle shape, Integer id, String name){
        super(world, shape, assetManager,id);
        this.name = name;
        this.font = assetManager.get("ui/default.fnt", BitmapFont.class); // Obtén la fuente del AssetManager
        this.layout = new GlyphLayout();

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData(this);
        box.dispose();

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_OTHERPLAYER;
        filter.maskBits = CollisionFilters.MASK_PLAYER;
        fixture.setFilterData(filter);

        setSpritePosModification(0f, getHeight()/4);
    }

    @Override
    public void act(float delta) {
        //stateMachine.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, name + " " + getId());
        font.draw(batch, layout, getX() - layout.width / 4, getY() + sprite.getHeight() + layout.height/4);
    }

}
