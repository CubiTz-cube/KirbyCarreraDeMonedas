package src.screens.game.gameLayers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.screens.components.OptionTable;

public class MenuGameLayer extends GameLayer {
    private final OptionTable optionTable;

    public MenuGameLayer(GameLayerManager gameLayerManager, Stage stage){
        super(gameLayerManager, stage, 2);

        ImageTextButton debugButton = new ImageTextButton("Modificar", manager.game.myImageTextbuttonStyle);
        debugButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                manager.changeLayer(GameLayerManager.LayerType.DEBUG);
            }
        });
        debugButton.addListener(manager.game.hoverListener);

        ImageTextButton exitButton = new ImageTextButton(manager.game.main.isClient() ? "Desconectarse": "Volver al Menu", manager.game.myImageTextbuttonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                manager.game.endGame();
            }
        });
        exitButton.addListener(manager.game.hoverListener);

        setZindex(0);
        optionTable = new OptionTable(manager.game.main.getSkin(), getLayer(), manager.game.main.fonts.briFont);
        getLayer().add(debugButton).width(400).padTop(10);
        getLayer().row();
        getLayer().add(exitButton).width(400).padTop(10);

        setZindex(1);
        getLayer().add(pauseBg).grow();

        setVisible(false);
    }



    @Override
    public void setVisible(Boolean visible) {
        super.setVisible(visible);
    }

    @Override
    public void update() {
        optionTable.update();
    }
}
