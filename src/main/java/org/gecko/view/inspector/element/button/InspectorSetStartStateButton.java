package org.gecko.view.inspector.element.button;

import org.gecko.viewmodel.SystemViewModel;

public class InspectorSetStartStateButton extends AbstractInspectorButton { //TODO rename to InspectorSetStartStateButton?

    private final SystemViewModel systemViewModel;

    public InspectorSetStartStateButton(SystemViewModel systemViewModel) {
        this.systemViewModel = systemViewModel;
    }
}
