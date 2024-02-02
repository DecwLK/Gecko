package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.InspectorColorPicker;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.RegionViewModel;

public class InspectorRegionColorItem extends LabeledInspectorElement {

    public InspectorRegionColorItem(ActionManager actionManager, RegionViewModel regionViewModel) {
        super(new InspectorLabel(ResourceHandler.getString("Inspector", "color")),
            new InspectorColorPicker(actionManager, regionViewModel));
    }
}
