package org.gecko.view.inspector;

import org.gecko.view.views.EditorView;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorFactory {

    private EditorView editorView;

    public InspectorFactory(EditorView editorView) {

    }

    public<T extends PositionableViewModelElement<?>> Inspector<T> createInspector(ViewElement<T> viewElement) {
        return null;
    }

    public Inspector<StateViewModel> createStateInspector(StateViewElement stateViewElement) {
        return null;
    }

    public Inspector<EdgeViewModel> createEdgeInspector(EdgeViewElement edgeViewElement) {
        return null;
    }

    public Inspector<SystemConnectionViewModel> createSystemConnectionInspector(SystemConnectionViewElement systemConnectionViewElement) {
        return null;
    }

    public Inspector<RegionViewModel> createRegionInspector(RegionViewElement regionViewElement) {
        return null;
    }

    public Inspector<SystemViewModel> createSystemInspector(SystemViewElement systemViewElement) {
        return null;
    }

    public Inspector<PortViewModel> createVariableBlockInspector(VariableBlockViewElement variableBlockViewElement) {
        return null;
    }
}
