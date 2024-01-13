package org.gecko.view;

import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.gecko.viewmodel.GeckoViewModel;

public class GeckoView {

    private final BorderPane mainPane;
    private final TabPane centerPane;
    private final GeckoViewModel viewModel;

    public GeckoView(GeckoViewModel viewModel) {
        this.viewModel = viewModel;
        this.mainPane = new BorderPane();
        this.centerPane = new TabPane();
    }
}
