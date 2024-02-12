package org.gecko.application;

import javafx.application.Application;


/**
 * The main class of the Gecko Graphic Editor for Contract Automata, serving as the entry point of the
 * {@link Application}. Provides the start method to configure and display the primary {@link javafx.stage.Stage} of the
 * application.
 */
public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}