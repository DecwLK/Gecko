package org.gecko.view.inspector.element.button;

import org.gecko.viewmodel.SystemViewModel;

public class InspectorOpenSystemButton extends AbstractInspectorButton {

    private final SystemViewModel systemViewModel;

    public InspectorOpenSystemButton(SystemViewModel systemViewModel) {
        this.systemViewModel = systemViewModel;
    }
}
