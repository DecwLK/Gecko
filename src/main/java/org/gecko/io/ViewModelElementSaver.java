package org.gecko.io;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.gecko.model.Automaton;
import org.gecko.model.Edge;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;

/**
 * Performs operations for every {@link org.gecko.model.Element Model-Element} from the subtree of a {@link System},
 * creating for each of them a {@link ViewModelPropertiesContainer}, depending on the attributes of the corresponding
 * {@link PositionableViewModelElement}.
 */
public class ViewModelElementSaver {
    private final GeckoViewModel geckoViewModel;
    private final List<ViewModelPropertiesContainer> viewModelProperties;
    @Getter
    private final List<StartStateContainer> startStates;

    ViewModelElementSaver(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        viewModelProperties = new ArrayList<>();
        startStates = new ArrayList<>();
    }

    protected List<ViewModelPropertiesContainer> getViewModelProperties(System root) {
        gatherSystemAttributes(root);
        return this.viewModelProperties;
    }

    private void gatherSystemAttributes(System system) {
        for (Variable variable : system.getVariables()) {
            this.savePortViewModelProperties(variable);
        }

        for (SystemConnection systemConnection : system.getConnections()) {
            this.saveSystemConnectionViewModelProperties(systemConnection);
        }

        Automaton automaton = system.getAutomaton();

        if (automaton.getStartState() != null) {
            StartStateContainer startStateContainer = new StartStateContainer();
            startStateContainer.setSystemId(system.getId());
            startStateContainer.setStartStateName(automaton.getStartState().getName());
            startStates.add(startStateContainer);
        }

        for (Region region : automaton.getRegions()) {
            this.saveRegionViewModelProperties(region);
        }

        for (State state : automaton.getStates()) {
            this.saveStateViewModelProperties(state);
        }

        for (Edge edge : automaton.getEdges()) {
            this.saveEdgeModelProperties(edge);
        }

        for (System child : system.getChildren()) {
            this.saveSystemViewModelProperties(child);
            this.gatherSystemAttributes(child);
        }
    }

    private void saveStateViewModelProperties(State state) {
        ViewModelPropertiesContainer stateViewModelContainer =
            this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(state));
        this.viewModelProperties.add(stateViewModelContainer);
    }

    private void saveRegionViewModelProperties(Region region) {
        ViewModelPropertiesContainer regionViewModelContainer =
            this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(region));

        Color color = ((RegionViewModel) this.geckoViewModel.getViewModelElement(region)).getColor();
        regionViewModelContainer.setRed(color.getRed());
        regionViewModelContainer.setGreen(color.getGreen());
        regionViewModelContainer.setBlue(color.getBlue());

        this.viewModelProperties.add(regionViewModelContainer);
    }

    private void saveSystemViewModelProperties(System system) {
        ViewModelPropertiesContainer systemViewModelContainer =
            this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(system));
        this.viewModelProperties.add(systemViewModelContainer);
    }

    private void saveSystemConnectionViewModelProperties(SystemConnection systemConnection) {
        ViewModelPropertiesContainer systemConnectionViewModelContainer =
            this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(systemConnection));
        this.viewModelProperties.add(systemConnectionViewModelContainer);
    }

    private void saveEdgeModelProperties(Edge edge) {
        ViewModelPropertiesContainer edgeViewModelContainer =
            this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(edge));
        this.viewModelProperties.add(edgeViewModelContainer);
    }

    private void savePortViewModelProperties(Variable variable) {
        ViewModelPropertiesContainer variableViewModelContainer =
            this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(variable));
        this.viewModelProperties.add(variableViewModelContainer);
    }

    ViewModelPropertiesContainer getCoordinateContainer(PositionableViewModelElement<?> element) {
        ViewModelPropertiesContainer container = new ViewModelPropertiesContainer();
        container.setElementId(element.getTarget().getId());
        container.setId(element.getId());
        container.setPositionX(element.getPosition().getX());
        container.setPositionY(element.getPosition().getY());
        container.setSizeX(element.getSize().getX());
        container.setSizeY(element.getSize().getY());
        return container;
    }
}
