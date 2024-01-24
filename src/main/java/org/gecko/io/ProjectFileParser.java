package org.gecko.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gecko.exceptions.MissingViewModelElement;
import org.gecko.model.Automaton;
import org.gecko.model.Edge;
import org.gecko.model.GeckoModel;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.viewmodel.GeckoViewModel;

public class ProjectFileParser implements FileParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Pair<GeckoModel, GeckoViewModel> parse(File file) throws IOException, MissingViewModelElement {
        GeckoJsonWrapper geckoJsonWrapper = objectMapper.readValue(file, GeckoJsonWrapper.class);

        System root = objectMapper.readValue(geckoJsonWrapper.getModel(), System.class);
        TypeReference<ArrayList<ViewModelPropertiesContainer>> typeRef = new TypeReference<>() {
        };
        List<ViewModelPropertiesContainer> newViewModelProperties = objectMapper.readValue(geckoJsonWrapper.getViewModelProperties(), typeRef);

        GeckoModel model = new GeckoModel(root);
        GeckoViewModel viewModel = new GeckoViewModel(model);

        ViewModelElementCreatorVisitor visitor = new ViewModelElementCreatorVisitor(viewModel.getViewModelFactory(), newViewModelProperties);
        this.visitModel(root, visitor);

        return new Pair<>(model, viewModel);
    }

    protected void visitModel(System system, ViewModelElementCreatorVisitor visitor) {
        for (Variable variable : system.getVariables()) {
            visitor.visit(variable);
        }

        for (SystemConnection systemConnection : system.getConnections()) {
            visitor.visit(systemConnection);
        }

        Automaton automaton = system.getAutomaton();

        for (State state : automaton.getStates()) {
            visitor.visit(state);
        }

        for (Region region : automaton.getRegions()) {
            visitor.visit(region);
        }

        for (Edge edge : automaton.getEdges()) {
            visitor.visit(edge);
        }

        for (System child : system.getChildren()) {
            visitor.visit(child);
            visitModel(child, visitor);
        }
    }
}
