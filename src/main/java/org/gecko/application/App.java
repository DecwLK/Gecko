package org.gecko.application;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Initialize Gecko
        stage.setTitle("Gecko");
        stage.show();
        GeckoManager geckoManager = new GeckoManager(stage);
        GeckoIOManager.getInstance().setGeckoManager(geckoManager);
        GeckoIOManager.getInstance().setStage(stage);
    }
}
