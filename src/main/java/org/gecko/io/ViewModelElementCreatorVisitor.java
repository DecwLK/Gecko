package org.gecko.io;

import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.gecko.exceptions.MissingViewModelElement;
import org.gecko.model.Automaton;
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
    @Getter
    private final List<PositionableViewModelElement<?>> generatedViewModelElements;
    ViewModelFactory viewModelFactory;
    HashMap<Integer, ViewModelPropertiesContainer> viewModelProperties;

    ViewModelElementCreatorVisitor(
        ViewModelFactory viewModelFactory, List<ViewModelPropertiesContainer> viewModelProperties,
        List<PositionableViewModelElement<?>> generatedViewModelElements) {
        this.viewModelFactory = viewModelFactory;
        this.viewModelProperties = new HashMap<>();
        for (ViewModelPropertiesContainer container : viewModelProperties) {
            this.viewModelProperties.put(container.getElementId(), container);
        }
        this.generatedViewModelElements = generatedViewModelElements;
    }

    private static void setPositionAndSize(
        PositionableViewModelElement<?> element, ViewModelPropertiesContainer container) {
        if (container != null) {
            element.setPosition(new Point2D(container.getPositionX(), container.getPositionY()));
            element.setSize(new Point2D(container.getSizeX(), container.getSizeY()));
        }
    }

    protected void visitModel(System system) {
        for (Variable variable : system.getVariables()) {
            visit(variable);
        }

        for (SystemConnection systemConnection : system.getConnections()) {
            visit(systemConnection);
        }

        Automaton automaton = system.getAutomaton();

        for (State state : automaton.getStates()) {
            visit(state);
        }

        for (Region region : automaton.getRegions()) {
            visit(region);
        }

        for (Edge edge : automaton.getEdges()) {
            visit(edge);
        }

        for (System child : system.getChildren()) {
            visit(child);
            visitModel(child);
        }
    }

    @Override
    public void visit(State state) {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelFrom(state);
        ViewModelPropertiesContainer container = viewModelProperties.get(state.getId());
        if (container == null) {
            generatedViewModelElements.add(stateViewModel);
        } else {
            setPositionAndSize(stateViewModel, container);
        }
    }

    @Override
    public void visit(Contract contract) {
    }

    @Override
    public void visit(SystemConnection systemConnection) {
        SystemConnectionViewModel systemConnectionViewModel = null;
        try {
            systemConnectionViewModel = viewModelFactory.createSystemConnectionViewModelFrom(systemConnection);
        } catch (MissingViewModelElement e) {
            PortViewModel source = viewModelFactory.createPortViewModelFrom(systemConnection.getSource());
            PortViewModel destination = viewModelFactory.createPortViewModelFrom(systemConnection.getDestination());
            generatedViewModelElements.add(source);
            generatedViewModelElements.add(destination);
            visit(systemConnection);
        }

        if (systemConnectionViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(systemConnection.getId());
            if (container == null) {
                generatedViewModelElements.add(systemConnectionViewModel);
            } else {
                setPositionAndSize(systemConnectionViewModel, container);
            }
        }
    }

    @Override
    public void visit(Variable variable) {
        PortViewModel portViewModel = viewModelFactory.createPortViewModelFrom(variable);
        ViewModelPropertiesContainer container = viewModelProperties.get(variable.getId());
        if (container == null) {
            generatedViewModelElements.add(portViewModel);
        } else {
            setPositionAndSize(portViewModel, container);
        }
    }

    @Override
    public void visit(System system) {
        SystemViewModel systemViewModel = viewModelFactory.createSystemViewModelFrom(system);
        ViewModelPropertiesContainer container = viewModelProperties.get(system.getId());
        if (container == null) {
            generatedViewModelElements.add(systemViewModel);
        } else {
            setPositionAndSize(systemViewModel, container);
        }
    }

    @Override
    public void visit(Region region) {
        RegionViewModel regionViewModel = null;
        try {
            regionViewModel = viewModelFactory.createRegionViewModelFrom(region);
        } catch (MissingViewModelElement e) {
            for (State state : region.getStates()) {
                StateViewModel stateViewModel = viewModelFactory.createStateViewModelFrom(state);
                generatedViewModelElements.add(stateViewModel);
            }
            visit(region);
        }

        if (regionViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(region.getId());
            if (container == null) {
                generatedViewModelElements.add(regionViewModel);
            } else {
                setPositionAndSize(regionViewModel, container);
                regionViewModel.setColor(Color.color(container.getRed(), container.getGreen(), container.getBlue()));
            }
        }
    }

    @Override
    public void visit(Edge edge) {
        EdgeViewModel edgeViewModel = null;
        try {
            edgeViewModel = viewModelFactory.createEdgeViewModelFrom(edge);
        } catch (MissingViewModelElement e) {
            StateViewModel source = viewModelFactory.createStateViewModelFrom(edge.getSource());
            StateViewModel destination = viewModelFactory.createStateViewModelFrom(edge.getDestination());
            generatedViewModelElements.add(source);
            generatedViewModelElements.add(destination);
            visit(edge);
        }

        if (edgeViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(edge.getId());
            if (container == null) {
                generatedViewModelElements.add(edgeViewModel);
            } else {
                setPositionAndSize(edgeViewModel, container);
            }
        }
    }
}
