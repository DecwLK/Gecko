package org.gecko.io;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import org.gecko.model.Automaton;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.ElementVisitor;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;

/**
 * Visitor performing operations for every {@link org.gecko.model.Element Model-Element}
 * from the subtree of a {@link System}, creating for each of them a {@link ViewModelPropertiesContainer},
 * depending on the attributes of the corresponding {@link PositionableViewModelElement}.
 */
public class ViewModelElementSaveVisitor implements ElementVisitor {
    private final GeckoViewModel geckoViewModel;
    private final List<ViewModelPropertiesContainer> viewModelProperties;

    protected ViewModelElementSaveVisitor(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.viewModelProperties = new ArrayList<>();
    }

    @Override
    public void visit(State state) {
        ViewModelPropertiesContainer stateViewModelContainer
            = this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(state));
        this.viewModelProperties.add(stateViewModelContainer);
    }

    @Override
    public void visit(Region region) {
        ViewModelPropertiesContainer regionViewModelContainer
            = this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(region));

        Color color = ((RegionViewModel) this.geckoViewModel.getViewModelElement(region)).getColor();
        regionViewModelContainer.setRed(color.getRed());
        regionViewModelContainer.setGreen(color.getGreen());
        regionViewModelContainer.setBlue(color.getBlue());

        this.viewModelProperties.add(regionViewModelContainer);
    }

    @Override
    public void visit(Contract contract) {
    }

    @Override
    public void visit(System system) {
        ViewModelPropertiesContainer systemViewModelContainer
            = this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(system));
        this.viewModelProperties.add(systemViewModelContainer);
    }

    @Override
    public void visit(SystemConnection systemConnection) {
        ViewModelPropertiesContainer systemConnectionViewModelContainer =
            this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(systemConnection));
        this.viewModelProperties.add(systemConnectionViewModelContainer);
    }

    @Override
    public void visit(Edge edge) {
        ViewModelPropertiesContainer edgeViewModelContainer
            = this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(edge));
        this.viewModelProperties.add(edgeViewModelContainer);
    }

    @Override
    public void visit(Variable variable) {
        ViewModelPropertiesContainer variableViewModelContainer
            = this.getCoordinateContainer(this.geckoViewModel.getViewModelElement(variable));
        this.viewModelProperties.add(variableViewModelContainer);
    }

    private ViewModelPropertiesContainer getCoordinateContainer(PositionableViewModelElement<?> element) {
        ViewModelPropertiesContainer container = new ViewModelPropertiesContainer();
        container.setElementId(element.getTarget().getId());
        container.setId(element.getId());
        container.setPositionX(element.getPosition().getX());
        container.setPositionY(element.getPosition().getY());
        container.setSizeX(element.getSize().getX());
        container.setSizeY(element.getSize().getY());
        return container;
    }

    protected List<ViewModelPropertiesContainer> getViewModelProperties(System root) {
        this.visitSystemAttributes(root);
        return this.viewModelProperties;
    }

    private void visitSystemAttributes(System system) {
        for (Variable variable : system.getVariables()) {
            this.visit(variable);
        }

        for (SystemConnection systemConnection : system.getConnections()) {
            this.visit(systemConnection);
        }

        Automaton automaton = system.getAutomaton();

        for (Region region : automaton.getRegions()) {
            this.visit(region);
        }

        for (State state : automaton.getStates()) {
            this.visit(state);
        }

        for (Edge edge : automaton.getEdges()) {
            this.visit(edge);
        }

        for (System child : system.getChildren()) {
            this.visit(child);
            this.visitSystemAttributes(child);
        }
    }
}
