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
    private final GlyphLayout layout;
    private final BitmapFont font;

    public OtherPlayer(World world, AssetManager assetManager, Float x, Float y, Integer id, String name, BitmapFont font) {
        super(world, x,y, assetManager,id);
        this.font = font;
        this.name = name;
        this.layout = new GlyphLayout();

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.OTHERPLAYER;
        filter.maskBits = (short)(~CollisionFilters.PLAYER & ~CollisionFilters.PROJECTIL);
        fixture.setFilterData(filter);

        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(bodyWidth/6);

        FixtureDef sensorFixtureDef = new FixtureDef();
        sensorFixtureDef.shape = sensorShape;
        sensorFixtureDef.isSensor = true;

        Fixture sensorFixture = body.createFixture(sensorFixtureDef);
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
        float textX = getX() + (sprite.getWidth() - layout.width) / 2;
        float textY = getY() + sprite.getHeight()/2 + layout.height / 2;
        font.draw(batch, layout, textX, textY);
    }
}
