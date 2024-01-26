package org.gecko.viewmodel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.gecko.model.Edge;

public class ViewModelElementDependencyFinderVisitor implements PositionableViewModelElementVisitor {

    private final GeckoViewModel geckoViewModel;
    private final SystemViewModel parent;
    private final Set<PositionableViewModelElement<?>> dependencies;

    public ViewModelElementDependencyFinderVisitor(GeckoViewModel geckoViewModel, SystemViewModel parent) {
        this.geckoViewModel = geckoViewModel;
        this.parent = parent;
        this.dependencies = new HashSet<>();
    }

    @Override
    public Set<PositionableViewModelElement<?>> visit(SystemViewModel systemViewModel) {
        return Collections.unmodifiableSet(dependencies);
    }

    @Override
    public Set<PositionableViewModelElement<?>> visit(RegionViewModel regionViewModel) {
        return Collections.unmodifiableSet(dependencies);
    }

    @Override
    public Set<PositionableViewModelElement<?>> visit(SystemConnectionViewModel systemConnectionViewModel) {
        return Collections.unmodifiableSet(dependencies);
    }

    @Override
    public Set<PositionableViewModelElement<?>> visit(EdgeViewModel edgeViewModel) {
        return Collections.unmodifiableSet(dependencies);
    }

    @Override
    public Set<PositionableViewModelElement<?>> visit(StateViewModel stateViewModel) {
        dependencies.add(stateViewModel);

        for (Edge edge : parent.getTarget().getAutomaton().getEdges()) {
            EdgeViewModel edgeViewModel = ((EdgeViewModel) geckoViewModel.getViewModelElement(edge));
            if (edgeViewModel.getSource().equals(stateViewModel) || edgeViewModel.getDestination()
                .equals(stateViewModel)) {
                dependencies.add(geckoViewModel.getViewModelElement(edge));
            }
        }

        return Collections.unmodifiableSet(dependencies);
    }

    @Override
    public Set<PositionableViewModelElement<?>> visit(PortViewModel portViewModel) {
        return Collections.unmodifiableSet(dependencies);
    }
}
