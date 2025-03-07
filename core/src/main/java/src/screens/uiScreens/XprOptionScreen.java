package src.screens.uiScreens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import src.main.Main;
import src.main.Modificable;
import src.screens.components.ColorPickerImage;
import src.screens.components.LayersManager;
import src.utils.constants.MyColors;
import src.world.entities.player.powers.PowerUp;

public class XprOptionScreen extends BlueCircleScreen {
    private final ScrollPane scrollPane;

    private final Image mainKirbyImage;
    private final ColorPickerImage colorWheel;
    private final TextField nameTextField;

    private final Slider damageSlider;
    private final Slider forceDashSlider;
    private final Slider walkAccelerationSlider;
    private final Slider maxWalkSpeedSlider;
    private final Slider runAccelerationSlider;
    private final Slider maxRunSpeedSlider;
    private final Slider maxJumpHoldTimeSlider;
    private final Slider jumpForceSlider;
    private final Slider sustainedJumpForceSlider;
    private final Slider absorbForceSlider;

    private final SelectBox<PowerUp.Type> initialPowerSelectBox;

    public XprOptionScreen(Main main) {
        super(main, "Modificaciones", null, Main.Screens.MENU);
        Skin skin = main.getSkin();

        Table optionsTable = new Table();
        scrollPane = new ScrollPane(optionsTable, skin);
        scrollPane.setFlickScroll(false);
        scrollPane.setClamp(true);

        Label colorLabel = new Label("Color", new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label nameLabel = new Label("Nombre", new Label.LabelStyle(main.fonts.interFont, Color.BLACK));

        Label damageLabel = new Label("Daño Dash: " + Modificable.DANO_DASH, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label forceDashLabel = new Label("Fuerza Impulso Dash: " + Modificable.FUERZA_IMPULSO_DASH, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label walkAccelerationLabel = new Label("Aceleración Caminar: " + Modificable.ACELERACION_CAMINAR, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label maxWalkSpeedLabel = new Label("Velocidad Máxima Caminar: " + Modificable.VELOCIDAD_MAXIMA_CAMINAR, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label runAccelerationLabel = new Label("Aceleración Correr: " + Modificable.ACELERACION_CORRER, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label maxRunSpeedLabel = new Label("Velocidad Máxima Correr: " + Modificable.VELOCIDAD_MAXIMA_CORRER, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label maxJumpHoldTimeLabel = new Label("Tiempo Máximo Salto Mantenido: " + Modificable.TIEMPO_MAXIMO_SALTO_MANTENIDO, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label jumpForceLabel = new Label("Fuerza Salto: " + Modificable.FUERZA_SALTO, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label sustainedJumpForceLabel = new Label("Fuerza Salto Sostenido: " + Modificable.FUERZA_SALTO_SOSTENIDO, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label absorbForceLabel = new Label("Fuerza Absorber: " + Modificable.FUERZA_ABSORBER, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));
        Label initialPowerLabel = new Label("Poder Inicial: " + Modificable.PODER_INICIAL, new Label.LabelStyle(main.fonts.interFont, Color.BLACK));

        Label mainKirbyName = new Label("Sin nombre", new Label.LabelStyle(main.fonts.interNameFont, MyColors.PINK));
        mainKirbyName.setAlignment(Align.center);
        mainKirbyImage = new Image(main.getAssetManager().get("ui/bg/kirbyIdleBg.png", Texture.class));
        mainKirbyImage.setScaling(Scaling.fit);
        mainKirbyImage.setAlign(Align.bottomLeft);

        nameTextField = new TextField("Sin nombre", myTextFieldStyle);
        nameTextField.setAlignment(Align.center);
        nameTextField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainKirbyName.setText(nameTextField.getText());
                main.setPlayerName(nameTextField.getText());
            }
        });

        colorWheel = new ColorPickerImage(main.getAssetManager().get("ui/colorWheel.png", Texture.class));
        colorWheel.setScaling(Scaling.fit);
        colorWheel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (main.playerColor.equals(colorWheel.getSelectColor())) return;
                mainKirbyImage.setColor(colorWheel.getSelectColor());
                main.playerColor.set(colorWheel.getSelectColor());
            }
        });
        colorWheel.addListener(hoverListener);

        damageSlider = new Slider(1, 30, 1, false, skin);
        damageSlider.setValue(Modificable.DANO_DASH);
        damageSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.DANO_DASH = (int)damageSlider.getValue();
                damageLabel.setText("Daño Dash: " + Modificable.DANO_DASH);
            }
        });

        forceDashSlider = new Slider(1, 100, 1, false, skin);
        forceDashSlider.setValue(Modificable.DANO_DASH);
        forceDashSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.FUERZA_IMPULSO_DASH = (int)forceDashSlider.getValue();
                forceDashLabel.setText("Fuerza Impulso Dash: " + Modificable.FUERZA_IMPULSO_DASH);
            }
        });

        walkAccelerationSlider = new Slider(1, 100, 1, false, skin);
        walkAccelerationSlider.setValue(Modificable.ACELERACION_CAMINAR);
        walkAccelerationSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.ACELERACION_CAMINAR = (int)walkAccelerationSlider.getValue();
                walkAccelerationLabel.setText("Aceleración Caminar: " + Modificable.ACELERACION_CAMINAR);
            }
        });

        maxWalkSpeedSlider = new Slider(1, 100, 1, false, skin);
        maxWalkSpeedSlider.setValue(Modificable.VELOCIDAD_MAXIMA_CAMINAR);
        maxWalkSpeedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.VELOCIDAD_MAXIMA_CAMINAR = (int)maxWalkSpeedSlider.getValue();
                maxWalkSpeedLabel.setText("Velocidad Máxima Caminar: " + Modificable.VELOCIDAD_MAXIMA_CAMINAR);
            }
        });

        runAccelerationSlider = new Slider(1, 100, 1, false, skin);
        runAccelerationSlider.setValue(Modificable.ACELERACION_CORRER);
        runAccelerationSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.ACELERACION_CORRER = (int)runAccelerationSlider.getValue();
                runAccelerationLabel.setText("Aceleración Correr: " + Modificable.ACELERACION_CORRER);
            }
        });

        maxRunSpeedSlider = new Slider(1, 100, 1, false, skin);
        maxRunSpeedSlider.setValue(Modificable.VELOCIDAD_MAXIMA_CORRER);
        maxRunSpeedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.VELOCIDAD_MAXIMA_CORRER = (int)maxRunSpeedSlider.getValue();
                maxRunSpeedLabel.setText("Velocidad Máxima Correr: " + Modificable.VELOCIDAD_MAXIMA_CORRER);
            }
        });

        maxJumpHoldTimeSlider = new Slider(1, 20, 0.1f, false, skin);
        maxJumpHoldTimeSlider.setValue(Modificable.TIEMPO_MAXIMO_SALTO_MANTENIDO);
        maxJumpHoldTimeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.TIEMPO_MAXIMO_SALTO_MANTENIDO = (int)maxJumpHoldTimeSlider.getValue();
                maxJumpHoldTimeLabel.setText("Tiempo Máximo Salto Mantenido: " + Modificable.TIEMPO_MAXIMO_SALTO_MANTENIDO);
            }
        });

        jumpForceSlider = new Slider(1, 100, 1, false, skin);
        jumpForceSlider.setValue(Modificable.FUERZA_SALTO);
        jumpForceSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.FUERZA_SALTO = (int)jumpForceSlider.getValue();
                jumpForceLabel.setText("Fuerza Salto: " + Modificable.FUERZA_SALTO);
            }
        });

        sustainedJumpForceSlider = new Slider(1, 50, 1, false, skin);
        sustainedJumpForceSlider.setValue(Modificable.FUERZA_SALTO_SOSTENIDO);
        sustainedJumpForceSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.FUERZA_SALTO_SOSTENIDO = (int)sustainedJumpForceSlider.getValue();
                sustainedJumpForceLabel.setText("Fuerza Salto Sostenido: " + Modificable.FUERZA_SALTO_SOSTENIDO);
            }
        });

        absorbForceSlider = new Slider(1, 150, 1, false, skin);
        absorbForceSlider.setValue(Modificable.FUERZA_ABSORBER);
        absorbForceSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.FUERZA_ABSORBER = (int)absorbForceSlider.getValue();
                absorbForceLabel.setText("Fuerza Absorber: " + Modificable.FUERZA_ABSORBER);
            }
        });

        initialPowerSelectBox = new SelectBox<>(skin);
        initialPowerSelectBox.setItems(PowerUp.Type.values());
        initialPowerSelectBox.setSelected(Modificable.PODER_INICIAL);
        initialPowerSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Modificable.PODER_INICIAL = initialPowerSelectBox.getSelected();
                initialPowerLabel.setText("Poder Inicial: " + Modificable.PODER_INICIAL);
            }
        });

        LayersManager layersManager = new LayersManager(stageUI, 1);

        layersManager.setZindex(0);
        layersManager.getLayer().setDebug(true);
        layersManager.getLayer().padTop(110);

        layersManager.getLayer().add(scrollPane).expand().fill();
        optionsTable.padLeft(200).padRight(200);
        optionsTable.add(mainKirbyName).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(mainKirbyImage).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(nameLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(nameTextField).expandX().fill().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(colorWheel).expand().pad(20).padBottom(10);
        optionsTable.row();
        optionsTable.add(colorLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(damageLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(damageSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(forceDashLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(forceDashSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(walkAccelerationLabel).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(walkAccelerationSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(maxWalkSpeedLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(maxWalkSpeedSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(runAccelerationLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(runAccelerationSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(maxRunSpeedLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(maxRunSpeedSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(maxJumpHoldTimeLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(maxJumpHoldTimeSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(jumpForceLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(jumpForceSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(sustainedJumpForceLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(sustainedJumpForceSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(absorbForceLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(absorbForceSlider).expandX().pad(20).padBottom(20);
        optionsTable.row();
        optionsTable.add(initialPowerLabel).expandX().pad(5);
        optionsTable.row();
        optionsTable.add(initialPowerSelectBox).expand().fill().height(60).pad(20).padBottom(20);

    }

    @Override
    public void show() {
        super.show();
        nameTextField.setText(main.getPlayerName());
        scrollPane.setScrollY(0);
    }
}
