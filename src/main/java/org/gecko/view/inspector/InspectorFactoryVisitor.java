package org.gecko.view.inspector;

import lombok.Getter;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

/** Follows the visitor pattern, implementing the {@link PositionableViewModelElementVisitor} interface. Holds a reference to the {@link InspectorFactory} and to the built {@link Inspector}. It uses the factory when visiting each type of {@link org.gecko.viewmodel.PositionableViewModelElement PositionableViewModelElement} in order to create inspectors for concrete {@link org.gecko.viewmodel.PositionableViewModelElement PositionableViewModelElement}s. */
public class InspectorFactoryVisitor implements PositionableViewModelElementVisitor {

    private final InspectorFactory inspectorFactory;
    @Getter
    private Inspector inspector;

    InspectorFactoryVisitor(InspectorFactory inspectorFactory) {
        this.inspectorFactory = inspectorFactory;
    }

    @Override
    public Object visit(SystemViewModel systemViewModel) {
        inspector = inspectorFactory.createSystemInspector(systemViewModel);
        return null;
    }

    @Override
    public Object visit(RegionViewModel regionViewModel) {
        inspector = inspectorFactory.createRegionInspector(regionViewModel);
        return null;
    }

    @Override
    public Object visit(SystemConnectionViewModel systemConnectionViewModel) {
        return null;
    }

    @Override
    public Object visit(EdgeViewModel edgeViewModel) {
        inspector = inspectorFactory.createEdgeInspector(edgeViewModel);
        return null;
    }

    @Override
    public Object visit(StateViewModel stateViewModel) {
        inspector = inspectorFactory.createStateInspector(stateViewModel);
        return null;
    }

    @Override
    public Object visit(PortViewModel portViewModel) {
        inspector = inspectorFactory.createVariableBlockInspector(portViewModel);
        return null;
    }
}
