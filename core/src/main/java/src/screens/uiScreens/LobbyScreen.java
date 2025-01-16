package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import src.main.Main;
import src.net.PacketListener;
import src.net.PlayerInfo;
import src.net.packets.Packet;
import src.screens.components.ColorField;
import src.screens.components.ColorPickerImage;
import src.screens.components.LayersManager;
import src.utils.constants.MyColors;

public class LobbyScreen extends UIScreen implements PacketListener {
    private final Table playersTable;

    private final ImageTextButton playButton;
    private final ColorPickerImage colorWheel;

    private final Image mainKirbyImage;
    private final Label mainKirbyName;

    public LobbyScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Label titleLabel = new Label("Partida", new Label.LabelStyle(main.getBriTitleFont(), Color.WHITE));
        titleLabel.setAlignment(Align.center);

        Texture pinkLineTexture = main.getAssetManager().get("ui/bg/pinkLineBg.png", Texture.class);
        pinkLineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image pinkLineImage = new Image(pinkLineTexture);

        Texture lineTexture = main.getAssetManager().get("ui/bg/lineBg.png", Texture.class);
        lineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image lineImage = new Image(lineTexture);

        Texture aroColorTexture = main.getAssetManager().get("ui/bg/aroColorPlayerBg.png", Texture.class);
        lineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image aroColorImage = new Image(aroColorTexture);
        aroColorImage.setScaling(Scaling.fit);
        aroColorImage.setAlign(Align.bottomLeft);

        mainKirbyName = new Label("Sin nombre", new Label.LabelStyle(main.getInterNameFont(), MyColors.PINK));
        mainKirbyName.setAlignment(Align.center);
        mainKirbyImage = new Image(main.getAssetManager().get("ui/bg/kirbyIdleBg.png", Texture.class));
        mainKirbyImage.setScaling(Scaling.fit);
        mainKirbyImage.setAlign(Align.bottomLeft);

        playersTable = new Table();
        ScrollPane scrollPane = new ScrollPane(playersTable, skin);
        scrollPane.setFillParent(true);

        playButton = new ImageTextButton("Empezar", myImageTextbuttonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.client.send(Packet.gameStart());
            }
        });
        playButton.addListener(hoverListener);

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exit.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exitHover.png", Texture.class));
        drawableHover.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        drawableUp.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawableUp;
        style.imageOver = drawableHover;

        ImageButton backButton = new ImageButton(style);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.closeClient();
                main.closeServer();
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });
        backButton.addListener(hoverListener);

        colorWheel = new ColorPickerImage(main.getAssetManager().get("ui/colorWheel.png", Texture.class));
        colorWheel.setScaling(Scaling.fit);
        colorWheel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.playerColor.set(colorWheel.getSelectColor());
                mainKirbyImage.setColor(colorWheel.getSelectColor());
                main.client.send(Packet.actEntityColor(-1, main.playerColor.r, main.playerColor.g, main.playerColor.b, main.playerColor.a));
            }
        });
        colorWheel.addListener(hoverListener);

        LayersManager layersManager = new LayersManager(stageUI, 9);

        layersManager.setZindex(0);
        layersManager.getLayer().add().expand(0,8);
        layersManager.getLayer().row();
        layersManager.getLayer().bottom().padLeft(20);
        layersManager.getLayer().add(mainKirbyName).left().growX().padBottom(10);
        layersManager.getLayer().add().expand(18,0);
        layersManager.getLayer().row();
        layersManager.getLayer().add(mainKirbyImage).grow();

        layersManager.setZindex(1);
        layersManager.getLayer().bottom();
        layersManager.getLayer().add(aroColorImage).expand(8,0).fill();
        layersManager.getLayer().add(colorWheel).expand(1,0).fill().pad(30).padBottom(20);
        layersManager.getLayer().add(playButton).expand(2,0).fill().pad(10).padBottom(120).padTop(100);

        layersManager.setZindex(2);
        layersManager.getLayer().top();
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().add(backButton).size(64).pad(25);

        layersManager.setZindex(3);
        scrollPane.setForceScroll(true, false);
        playersTable.bottom();
        layersManager.getLayer().add(scrollPane).expand().fill();
        layersManager.getLayer().row();
        layersManager.getLayer().add().expand(0,2);

        layersManager.setZindex(4);
        layersManager.getLayer().top();
        layersManager.getLayer().add(titleLabel).padTop(15).width(398).center();
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(5);
        layersManager.getLayer().top();
        layersManager.getLayer().add(pinkLineImage).padTop(50).expandX().left();

        layersManager.setZindex(6);
        layersManager.getLayer().top();
        layersManager.getLayer().add(lineImage).expandX().fillX().padTop(76);

    }

    @Override
    public void show() {
        super.show();
        playButton.setVisible(main.server != null);
        mainKirbyName.setText(main.getName());
        main.client.addListener(this);
        updatePlayersTable();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void updatePlayersTable() {
        playersTable.clear();
        for (PlayerInfo player : main.client.getPlayersConnected().values()) {
            Label nameLabel = new Label(player.getName(), new Label.LabelStyle(main.getInterNameFont(), MyColors.PINK));
            nameLabel.setAlignment(Align.center);
            playersTable.add(nameLabel).pad(25).expandX().fillX().center();
        }
        playersTable.row();

        for (PlayerInfo player : main.client.getPlayersConnected().values()) {
            Image kirbyImage = new Image(main.getAssetManager().get("ui/bg/kirbyIdleBg.png", Texture.class));
            kirbyImage.setColor(player.getColor());
            kirbyImage.setScaling(Scaling.fit);
            playersTable.add(kirbyImage).pad(10).expandX().fillX().center();
        }
    }

    @Override
    public void hide() {
        playersTable.clear();
    }

    @Override
    public void receivedPacket(Packet.Types type) {
        if (type.equals(Packet.Types.GAMESTART)) {
            Gdx.app.postRunnable(() -> main.changeScreen(Main.Screens.GAME));
        }
        if (main.client.gameStart) return;
        if (type.equals(Packet.Types.NEWPLAYER) || type.equals(Packet.Types.DISCONNECTPLAYER) || type.equals(Packet.Types.ACTENTITYCOLOR)) {
            Gdx.app.postRunnable(this::updatePlayersTable);
        }
    }

    @Override
    public void closeClient() {
        if (main.client.gameStart) return;
        Gdx.app.postRunnable(() -> main.changeScreen(Main.Screens.MULTIPLAYER));
    }

    @Override
    public void dispose() {
        super.dispose();
        colorWheel.dispose();
    }
}
