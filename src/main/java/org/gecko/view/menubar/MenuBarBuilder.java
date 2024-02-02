package org.gecko.view.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import org.gecko.actions.ActionManager;
import org.gecko.view.GeckoView;
import org.gecko.view.ResourceHandler;

public class MenuBarBuilder {

    private final MenuBar menuBar;
    private final GeckoView view;
    private final ActionManager actionManager;

    public MenuBarBuilder(GeckoView view, ActionManager actionManager) {
        this.view = view;
        this.actionManager = actionManager;
        menuBar = new MenuBar();

        // TODO
        menuBar.getMenus()
            .addAll(new Menu(ResourceHandler.getString("Labels", "file")),
                new Menu(ResourceHandler.getString("Labels", "edit")),
                new Menu(ResourceHandler.getString("Labels", "view")),
                new Menu(ResourceHandler.getString("Labels", "tools")),
                new Menu(ResourceHandler.getString("Labels", "help")));
    }

    public MenuBar build() {
        return menuBar;
    }
}
