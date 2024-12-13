package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.net.PacketListener;
import src.net.PlayerInfo;
import src.net.packets.Packet;
import src.screens.components.ColorField;

public class LobbyScreen extends UIScreen implements PacketListener {
    private final Table playersTable;
    private Integer numPlayersConnected;

    private final Table table;
    private final Label titleLabel;
    private final ScrollPane scrollPane;
    private final TextButton playButton;
    private final TextButton backButton;
    private final ColorField colorField;

    public LobbyScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        titleLabel = new Label("Lobby", skin);

        playersTable = new Table();

        scrollPane = new ScrollPane(playersTable, skin);
        scrollPane.setFillParent(true);

        playButton = new TextButton("Empezar Partida", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.client.send(Packet.gameStart());
            }
        });

        backButton = new TextButton("Salirse", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.closeClient();
                main.closeServer();
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });

        colorField = new ColorField(skin);
        colorField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.playerColor.set(colorField.getColor());
                main.client.send(Packet.actEntityColor(-1, main.playerColor.r, main.playerColor.g, main.playerColor.b, main.playerColor.a));
            }
        });
    }

    @Override
    public void show() {
        super.show();
        numPlayersConnected = 0;
        table.add(titleLabel).pad(10);
        table.row();
        table.add(scrollPane).expand().fill();
        table.row();
        if (main.server != null) {
            table.add(playButton).width(200).height(50).pad(10);
            table.row();
        }
        table.add(colorField);
        table.add(backButton).width(200).height(50).pad(10);
        main.client.addListener(this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void updatePlayersTable() {
        playersTable.clear();
        for (PlayerInfo player : main.client.getPlayersConnected().values()) {
            Label nameLabel = new Label(player.getName(), main.getSkin());
            nameLabel.setColor(player.getColor());
            //System.out.println("Color a la hora de crear la label de "+ player.getName() + " " + player.getColor());
            playersTable.add(nameLabel).pad(25);
            playersTable.row();
        }
    }

    @Override
    public void hide() {
        playersTable.clear();
        table.clear();
    }

    @Override
    public void receivedPacket(Packet.Types type) {
        if (type.equals(Packet.Types.GAMESTART)){
            main.changeScreen(Main.Screens.GAME);
        }
        if (main.client.gameStart) return;
        if (type.equals(Packet.Types.NEWPLAYER) || type.equals(Packet.Types.DISCONNECTPLAYER) || type.equals(Packet.Types.ACTENTITYCOLOR)) {
            numPlayersConnected = main.client.getPlayersConnected().size();
            updatePlayersTable();
        }
    }
}
