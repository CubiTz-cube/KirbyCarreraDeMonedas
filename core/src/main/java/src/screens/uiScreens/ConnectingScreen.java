package src.screens.uiScreens;

import src.main.Main;

public class ConnectingScreen extends UIScreen {
    public ConnectingScreen(Main main) {
        super(main);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (main.client != null && main.client.isRunning()) {
            main.changeScreen(Main.Screens.LOBBYCLIENT);
        }
    }
}
