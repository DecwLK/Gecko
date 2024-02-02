package org.gecko.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize Gecko
        GeckoManager geckoManager = new GeckoManager();
        GeckoIOManager.getInstance().setGeckoManager(geckoManager);
        GeckoIOManager.getInstance().setStage(stage);

        stage.setTitle("Gecko");

        Scene scene = new Scene(geckoManager.getGecko().getView().getMainPane(), 1024, 768);
        stage.setScene(scene);
        // currently not used
        geckoManager.getGecko().getView().postInit();

        stage.show();
    }
}
