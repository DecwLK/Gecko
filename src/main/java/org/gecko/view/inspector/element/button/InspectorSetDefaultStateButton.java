package org.gecko.view.inspector.element.button;

import org.gecko.viewmodel.SystemViewModel;

public class InspectorSetDefaultStateButton extends AbstractInspectorButton { //TODO rename to InspectorSetStartStateButton?

    private final SystemViewModel systemViewModel;

    public InspectorSetDefaultStateButton(SystemViewModel systemViewModel) {
        this.systemViewModel = systemViewModel;
    }
}
