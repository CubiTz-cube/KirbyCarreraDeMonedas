package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;

public class MultiplayerScreen extends UIScreen {

    public MultiplayerScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton joinButton = new TextButton("Unirse", skin);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        TextButton createButton = new TextButton("Crear", skin);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        TextButton backButton = new TextButton("Volver", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MENU);
            }
        });

        table.add(joinButton).width(200).height(50).pad(10);
        table.row();
        table.add(createButton).width(200).height(50).pad(10);
        table.row();
        table.add(backButton).width(200).height(50).pad(10);
    }
}
