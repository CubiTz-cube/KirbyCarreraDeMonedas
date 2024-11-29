package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.net.packets.Packet;

public class LobbyScreen extends UIScreen {
    private final Table playersTable;
    private Integer numPlayersConnected;

    private Table table;
    private Label titleLabel;
    private ScrollPane scrollPane;
    private TextButton playButton;
    private TextButton backButton;

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
        table.add(backButton).width(200).height(50).pad(10);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (numPlayersConnected != main.client.getPlayersConnected().size()) {
            numPlayersConnected = main.client.getPlayersConnected().size();
            updatePlayersTable();
        }
        if (main.client.gameStart){
            main.changeScreen(Main.Screens.GAME);
        }
    }

    private void updatePlayersTable() {
        playersTable.clear();
        for (String player : main.client.getPlayersConnected().values()) {
            playersTable.add(new Label(player, main.getSkin())).pad(25);
            playersTable.row();
        }
    }

    @Override
    public void hide() {
        playersTable.clear();
        table.clear();
    }
}
