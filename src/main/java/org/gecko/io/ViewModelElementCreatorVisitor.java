package org.gecko.io;

import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.exceptions.MissingViewModelElement;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.ElementVisitor;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class ViewModelElementCreatorVisitor implements ElementVisitor {
    ViewModelFactory viewModelFactory;
    HashMap<Integer, ViewModelPropertiesContainer> viewModelProperties;

    ViewModelElementCreatorVisitor(ViewModelFactory viewModelFactory,
                                   List<ViewModelPropertiesContainer> viewModelProperties) {
        this.viewModelFactory = viewModelFactory;
        this.viewModelProperties = new HashMap<>();
        for (ViewModelPropertiesContainer container : viewModelProperties) {
            this.viewModelProperties.put(container.getElementId(), container);
        }
    }

    private static void setPositionAndSize(PositionableViewModelElement<?> element, ViewModelPropertiesContainer container) {
        element.setPosition(new Point2D(container.getPositionX(), container.getPositionY()));
        element.setSize(new Point2D(container.getSizeX(), container.getSizeY()));
    }

    @Override
    public void visit(State state) {
        StateViewModel stateViewModel = this.viewModelFactory.createStateViewModelFrom(state);
        ViewModelPropertiesContainer container = this.viewModelProperties.get(state.getId());
        setPositionAndSize(stateViewModel, container);
    }

    @Override
    public void visit(Contract contract) {
    }

    @Override
    public void visit(SystemConnection systemConnection) {
        SystemConnectionViewModel systemConnectionViewModel = null;
        try {
            systemConnectionViewModel = this.viewModelFactory.createSystemConnectionViewModelFrom(systemConnection);
        } catch (MissingViewModelElement e) {
            // TODO: Shouldn't actually handle here, but throwing exception causes exception-throw waterfall.
        }

        if (systemConnectionViewModel != null) {
            ViewModelPropertiesContainer container = this.viewModelProperties.get(systemConnection.getId());
            setPositionAndSize(systemConnectionViewModel, container);
        }
    }

    @Override
    public void visit(Variable variable) {
        PortViewModel portViewModel = this.viewModelFactory.createPortViewModelFrom(variable);
        ViewModelPropertiesContainer container = this.viewModelProperties.get(variable.getId());
        setPositionAndSize(portViewModel, container);
    }

    @Override
    public void visit(System system) {
        SystemViewModel systemViewModel = this.viewModelFactory.createSystemViewModelFrom(system);
        ViewModelPropertiesContainer container = this.viewModelProperties.get(system.getId());
        setPositionAndSize(systemViewModel, container);
    }

    @Override
    public void visit(Region region) {
        RegionViewModel regionViewModel = null;
        try {
            regionViewModel = this.viewModelFactory.createRegionViewModelFrom(region);
        } catch (MissingViewModelElement e) {
                // TODO: Shouldn't actually handle here, but throwing exception causes exception-throw waterfall.
        }

        if (regionViewModel != null) {
            ViewModelPropertiesContainer container = this.viewModelProperties.get(region.getId());
            setPositionAndSize(regionViewModel, container);
            regionViewModel.setColor(Color.color(container.getRed(), container.getGreen(), container.getBlue()));
        }
    }

    @Override
    public void visit(Edge edge) {
        EdgeViewModel edgeViewModel = null;
        try {
            edgeViewModel = this.viewModelFactory.createEdgeViewModelFrom(edge);
        } catch (MissingViewModelElement e) {
            // TODO: Shouldn't actually handle here, but throwing exception causes exception-throw waterfall.
        }

        if (edgeViewModel != null) {
            ViewModelPropertiesContainer container = this.viewModelProperties.get(edge.getId());
            setPositionAndSize(edgeViewModel, container);
        }
    }
}
