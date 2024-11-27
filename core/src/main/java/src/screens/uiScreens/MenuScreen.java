package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;

public class MenuScreen extends UIScreen {

    public MenuScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);

        TextButton playButton = new TextButton("Jugar", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.GAME);
            }
        });

        TextButton multiplayerButton = new TextButton("Multijugador", skin);
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });

        TextButton optionButton = new TextButton("Configuracion", skin);
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.OPTION);
            }
        });

        TextButton infoButton = new TextButton("Acerca de", skin);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.INFO);
            }
        });

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(playButton).width(200).height(50).pad(10);
        table.row();
        table.add(multiplayerButton).width(200).height(50).pad(10);
        table.row();
        table.add(optionButton).width(200).height(50).pad(10);
        table.row();
        table.add(infoButton).width(200).height(50).pad(10);
        table.row();
        table.add(exitButton).width(200).height(50).pad(10);
    }
}
