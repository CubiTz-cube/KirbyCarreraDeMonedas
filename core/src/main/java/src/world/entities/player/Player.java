package src.world.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.main.Main;
import src.screens.GameScreen;
import src.utils.CollisionFilters;
import src.utils.variables.PlayerControl;
import src.world.ActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.mirror.Mirror;
import src.world.entities.player.powers.PowerUp;
import src.world.entities.player.states.*;

public class Player extends PlayerCommon {
    private Boolean changeAnimation;

    public Enemy enemyAbsorded;

    public final GameScreen game;

    public Player(World world, Rectangle shape, AssetManager assetManager, GameScreen game) {
        super(world, shape, assetManager, -1);
        this.game = game;
        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_PLAYER;
        filter.maskBits = ~CollisionFilters.MASK_OTHERPLAYER;
        fixture.setFilterData(filter);

        setSpritePosModification(0f, getHeight()/4);

        initStates();
        setCurrentState(StateType.IDLE);

        changeAnimation = false;
    }

    private void initStates(){
        idleState = new IdleState(this);
        jumpState = new JumpState(this);
        walkState = new WalkState(this);
        fallState = new FallState(this);
        flyState = new FlyState(this);
        downState = new DownState(this);
        absorbState = new AbsorbState(this);
        dashState = new DashState(this);
        runState = new RunState(this);
        stunState = new StunState(this);
        consumeState = new ConsumeState(this);
    }

    public void consumeEnemy() {
        if (enemyAbsorded == null) return;
        PowerUp.Type powerType = enemyAbsorded.getPowerUp();
        enemyAbsorded = null;
        setCurrentState(PlayerCommon.StateType.IDLE);
        setCurrentPowerUp(powerType);
    }

    @Override
    public void setAnimation(AnimationType animationType) {
        super.setAnimation(animationType);
        changeAnimation = true;
    }

    public Boolean checkChangeAnimation() {
        if (changeAnimation) {
            changeAnimation = false;
            return true;
        }
        return false;
    }

    @Override
    public void setFlipX(Boolean flipX) {
        super.setFlipX(flipX);
        changeAnimation = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 velocity = body.getLinearVelocity();

        if (getCurrentStateType() == StateType.DASH || getCurrentStateType() == StateType.STUN) return;
        if (!Gdx.input.isKeyPressed(PlayerControl.LEFT) && !Gdx.input.isKeyPressed(PlayerControl.RIGHT)){
            float brakeForce = 10f;
            body.applyForce(-velocity.x * brakeForce, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Enemy enemy) {
            if (getCurrentStateType() == StateType.ABSORB){
                enemyAbsorded = enemy;
                game.removeEntity(enemy.getId());
                setCurrentState(Player.StateType.IDLE);
                return;
            }

            Vector2 pushDirection = body.getPosition().cpy().sub(actor.getBody().getPosition()).nor();
            if (getCurrentStateType() == StateType.DASH && enemy.getCurrentStateType() != Enemy.StateType.DAMAGE){
                enemy.takeDamage(1);
                body.setLinearVelocity(0,0);
                body.applyLinearImpulse(pushDirection.scl(5f), body.getWorldCenter(), true);
                body.applyLinearImpulse(0,5f, body.getWorldCenter().x, body.getWorldCenter().y, true);
                setCurrentState(StateType.FALL);
                return;
            }

            if (getCurrentStateType() == StateType.STUN) return;
            setCurrentState(Player.StateType.STUN);
            body.applyLinearImpulse(pushDirection.scl(15f), body.getWorldCenter(), true);

        } else if (actor instanceof Mirror) {
            game.threadSecureWorld.addModification(() -> {
                game.getPlayer().getBody().setTransform(game.lobbyPlayer.x, game.lobbyPlayer.y, 0);
                game.main.changeScreen(Main.Screens.MINIDUCK);
                game.randomMirror();
            });
        }
    }
}
