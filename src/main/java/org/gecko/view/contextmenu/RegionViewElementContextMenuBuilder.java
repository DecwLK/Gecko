package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.RegionViewModel;

public class RegionViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final RegionViewModel regionViewModel;

    public RegionViewElementContextMenuBuilder(ActionManager actionManager, EditorView editorView, RegionViewModel regionViewModel) {
        super(actionManager, editorView);

        this.regionViewModel = regionViewModel;
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
