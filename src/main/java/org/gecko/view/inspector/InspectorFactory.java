package org.gecko.view.inspector;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.builder.*;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.*;

public class InspectorFactory {

    private final EditorView editorView;
    private final EditorViewModel editorViewModel;
    private final ActionManager actionManager;

    public InspectorFactory(ActionManager actionManager, EditorView editorView, EditorViewModel editorViewModel) {
        this.actionManager = actionManager;
        this.editorView = editorView;
        this.editorViewModel = editorViewModel;
    }

    public Inspector createInspector(PositionableViewModelElement<?> viewElement) {
        InspectorFactoryVisitor visitor = new InspectorFactoryVisitor(this);
        viewElement.accept(visitor);
        return visitor.getInspector();
    }

    public Inspector createStateInspector(StateViewModel stateViewModel) {
        return buildInspector(new StateInspectorBuilder(actionManager, editorViewModel, stateViewModel));
    }

    public Inspector createEdgeInspector(EdgeViewModel edgeViewModel) {
        return buildInspector(new EdgeInspectorBuilder(actionManager, edgeViewModel));
    }

    public Inspector createRegionInspector(RegionViewModel regionViewModel) {
        return buildInspector(new RegionInspectorBuilder(actionManager, regionViewModel));
    }

    public Inspector createSystemInspector(SystemViewModel systemViewModel) {
        return buildInspector(new SystemInspectorBuilder(actionManager, systemViewModel));
    }

    public Inspector createVariableBlockInspector(PortViewModel portviewModel) {
        return buildInspector(new VariableBlockInspectorBuilder(actionManager, portviewModel));
    }

    private Inspector buildInspector(AbstractInspectorBuilder<?> builder) {
        return builder.build(editorView, editorViewModel);
    }
}
