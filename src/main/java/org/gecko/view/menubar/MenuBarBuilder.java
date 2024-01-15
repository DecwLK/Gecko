package org.gecko.view.menubar;

import javafx.scene.control.MenuBar;
import org.gecko.view.GeckoView;

public class MenuBarBuilder {

    private final MenuBar menuBar;
    private final GeckoView view;

    public MenuBarBuilder(GeckoView view) {
        this.view = view;
        menuBar = new MenuBar();
    }

    public MenuBar build() {
        return null;
    }
}
