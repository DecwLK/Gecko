package org.gecko.view.inspector;

import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.builder.AbstractInspectorBuilder;
import org.gecko.view.inspector.builder.EdgeInspectorBuilder;
import org.gecko.view.inspector.builder.RegionInspectorBuilder;
import org.gecko.view.inspector.builder.StateInspectorBuilder;
import org.gecko.view.inspector.builder.SystemInspectorBuilder;
import org.gecko.view.inspector.builder.VariableBlockInspectorBuilder;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorFactory {

    @Getter private final EditorView editorView;
    @Getter private final EditorViewModel editorViewModel;
    @Getter private final ActionManager actionManager;

    public InspectorFactory(
            ActionManager actionManager, EditorView editorView, EditorViewModel editorViewModel) {
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
        return buildInspector(
                new EdgeInspectorBuilder(actionManager, editorViewModel, edgeViewModel));
    }

    public Inspector createRegionInspector(RegionViewModel regionViewModel) {
        return buildInspector(new RegionInspectorBuilder(actionManager, regionViewModel));
    }

    public Inspector createSystemInspector(SystemViewModel systemViewModel) {
        return buildInspector(new SystemInspectorBuilder(actionManager, editorViewModel, systemViewModel));
    }

    public Inspector createVariableBlockInspector(PortViewModel portviewModel) {
        return buildInspector(new VariableBlockInspectorBuilder(actionManager, portviewModel));
    }

    private Inspector buildInspector(AbstractInspectorBuilder<?> builder) {
        return builder.build(editorView, editorViewModel);
    }
}
