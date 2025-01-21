package src.screens.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

public class LayersManager {
    private final ArrayList<Table> layers;
    private Integer zIndex;

    public LayersManager(Stage stage, Integer numLayers) {
        layers = new ArrayList<>();
        for (int i = 0; i < numLayers; i++) {
            Table table = new Table();
            table.setFillParent(true);
            layers.add(table);
        }
        for (int i = numLayers-1; i >= 0; i--) {
            stage.addActor(layers.get(i));
        }
    }

    /**
     * Cambia la capa que se obtiene con getLayer
     * @param zIndex índice de la capa
     */
    public void setZindex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Obtiene la capa seleccionada con setZindex
      * @return
     */
    public Table getLayer() {
        return layers.get(zIndex);
    }

    public void setVisible(Boolean visible) {
        for (Table layer : layers) {
            layer.setVisible(visible);
        }
    }

    public void clear() {
        for (Table layer : layers) {
            layer.clear();
        }
    }

    public Boolean isVisible() {
        return layers.get(0).isVisible();
    }

    public void setPosition(Float x, Float y) {
        for (Table layer : layers) {
            layer.setPosition(x, y);
        }
    }

    public void setCenterPosition(Float x, Float y) {
        for (Table layer : layers) {
            layer.setPosition(x - layer.getWidth() / 2, y - layer.getHeight() / 2);
        }
    }
}
