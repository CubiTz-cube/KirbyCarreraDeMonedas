package src.world.entities.otherPlayer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.constants.CollisionFilters;
import src.world.entities.NoAutoPacketEntity;
import src.world.entities.player.PlayerCommon;

public class OtherPlayer extends PlayerCommon implements NoAutoPacketEntity {
    private final String name;
    private final BitmapFont font;
    private final GlyphLayout layout;

    private final Fixture sensorFixture;

    public OtherPlayer(World world, AssetManager assetManager, Float x, Float y, Integer id, String name){
        super(world, x,y, assetManager,id);
        this.name = name;
        this.font = assetManager.get("ui/default.fnt", BitmapFont.class); // Obtén la fuente del AssetManager
        this.layout = new GlyphLayout();

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.OTHERPLAYER;
        filter.maskBits = (short)~CollisionFilters.PLAYER;
        fixture.setFilterData(filter);

        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(bodyWidth / 2, bodyHeight / 2);

        FixtureDef sensorFixtureDef = new FixtureDef();
        sensorFixtureDef.shape = sensorShape;
        sensorFixtureDef.isSensor = true;

        sensorFixture = body.createFixture(sensorFixtureDef);
        sensorFixture.setUserData(this);
        sensorShape.dispose();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void act(float delta) {
        //stateMachine.update(delta);
    }

    @Override
    public void setCurrentState(StateType stateType) {
        currentStateType = stateType;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, name);
        font.draw(batch, layout, getX() - layout.width / 4, getY() + sprite.getHeight() + layout.height/4);
    }
}
