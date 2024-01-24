package org.gecko.io;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.gecko.exceptions.MissingViewModelElement;
import org.gecko.model.Automaton;
import org.gecko.model.Edge;
import org.gecko.model.GeckoModel;
import org.gecko.model.ModelFactory;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.Test;

public class ProjectFileParserTest {
    @Test
    void createGeckoFromFile() {
        Pair<GeckoModel, GeckoViewModel> setupPair = null;
        try {
            setupPair = setup();
        } catch (MissingViewModelElement e) {
            java.lang.System.err.println(e.getMessage());
        }
        ProjectFileSerializer projectFileSerializer = new ProjectFileSerializer();
        try {
            projectFileSerializer.createFile(setupPair.getKey(), setupPair.getValue(), "json/projectSetup.json");
        } catch (IOException e) {
            fail();
        }

        ProjectFileParser projectFileParser = new ProjectFileParser();
        Pair<GeckoModel, GeckoViewModel> geckoPair = null;
        try {
            geckoPair = projectFileParser.parse(new File("json/projectSetup.json"));
        } catch (IOException e) {
            java.lang.System.err.println(e.getMessage());
            fail();
        } catch (MissingViewModelElement e) {
            java.lang.System.err.println(e.getMessage());
        }

        ProjectFileSerializer projectFileSerializer2 = new ProjectFileSerializer();
        try {
            projectFileSerializer2.createFile(geckoPair.getKey(), geckoPair.getValue(), "json/project.json");
        } catch (IOException e) {
            fail();
        }

        try {
            String setupFile = new String(Files.readAllBytes(Paths.get("json/projectSetup.json")));
            String parsedFile = new String(Files.readAllBytes(Paths.get("json/project.json")));
            assertNotNull(setupFile);
            assertNotNull(parsedFile);
        } catch (IOException e) {
            fail();
        }
    }

    Pair<GeckoModel, GeckoViewModel> setup() throws MissingViewModelElement {
        // Initialize Gecko Model and ViewModel.
        // Consists of a root-System with three child-Systems and six Variables.
        // All Systems have Automatons. Children's Automatons have Regions.
        // All States have two Contracts each.
        // There exist Edges and SystemConnections between index pairs (i, i+1);

        GeckoModel model = new GeckoModel();
        GeckoViewModel viewModel = new GeckoViewModel(model);
        ModelFactory modelFactory = model.getModelFactory();
        ViewModelFactory viewModelFactory = viewModel.getViewModelFactory();

        System root = model.getRoot();
        SystemViewModel rootVM = viewModelFactory.createSystemViewModelFrom(root);

        viewModel.addViewModelElement(rootVM);

        getNewAutomaton(viewModel, modelFactory, viewModelFactory, root, 2, 0);

        // Children:
        System child1 = modelFactory.createSystem(root);
        SystemViewModel child1VM = viewModelFactory.createSystemViewModelFrom(child1);
        System child2 = modelFactory.createSystem(root);
        SystemViewModel child2VM = viewModelFactory.createSystemViewModelFrom(child2);
        System child3 = modelFactory.createSystem(root);
        SystemViewModel child3VM = viewModelFactory.createSystemViewModelFrom(child3);

        viewModel.addViewModelElement(child1VM);
        viewModel.addViewModelElement(child2VM);
        viewModel.addViewModelElement(child3VM);

        // Variables:
        Variable var1 = modelFactory.createVariable(root);
        var1.setVisibility(Visibility.INPUT);
        PortViewModel var1VM = viewModelFactory.createPortViewModelFrom(var1);

        Variable var2 = modelFactory.createVariable(root);
        var2.setVisibility(Visibility.OUTPUT);
        PortViewModel var2VM = viewModelFactory.createPortViewModelFrom(var2);

        Variable var3 = modelFactory.createVariable(root);
        var3.setVisibility(Visibility.INPUT);
        PortViewModel var3VM = viewModelFactory.createPortViewModelFrom(var3);

        Variable var4 = modelFactory.createVariable(root);
        var4.setVisibility(Visibility.OUTPUT);
        PortViewModel var4VM = viewModelFactory.createPortViewModelFrom(var4);

        Variable var5 = modelFactory.createVariable(root);
        var5.setVisibility(Visibility.INPUT);
        PortViewModel var5VM = viewModelFactory.createPortViewModelFrom(var5);

        Variable var6 = modelFactory.createVariable(root);
        var6.setVisibility(Visibility.OUTPUT);
        PortViewModel var6VM = viewModelFactory.createPortViewModelFrom(var6);

        viewModel.addViewModelElement(var1VM);
        viewModel.addViewModelElement(var2VM);
        viewModel.addViewModelElement(var3VM);
        viewModel.addViewModelElement(var4VM);
        viewModel.addViewModelElement(var5VM);
        viewModel.addViewModelElement(var6VM);

        // System Connections
        List<Variable> variables = new ArrayList<>(root.getVariables());
        SystemConnection connection1 = modelFactory.createSystemConnection(root, variables.get(1), variables.get(2));
        SystemConnectionViewModel connection1VM = viewModelFactory.createSystemConnectionViewModelFrom(connection1);
        SystemConnection connection2 = modelFactory.createSystemConnection(root, variables.get(3), variables.get(4));
        SystemConnectionViewModel connection2VM = viewModelFactory.createSystemConnectionViewModelFrom(connection2);

        viewModel.addViewModelElement(connection1VM);
        viewModel.addViewModelElement(connection2VM);

        // Automatons for children:
        for (System child : root.getChildren()) {
            getNewAutomaton(viewModel, modelFactory, viewModelFactory, child, 3, 2);
        }

        return new Pair<>(model, viewModel);
    }

    void getNewAutomaton(GeckoViewModel viewModel, ModelFactory factory, ViewModelFactory viewModelFactory, System parentSystem, int numberOfStates, int statesInRegion)
        throws MissingViewModelElement{
        Automaton automaton = factory.createAutomaton(parentSystem);

        // States:
        for (int i = 0; i < numberOfStates; i++) {
            State state = factory.createState(parentSystem.getAutomaton());
            factory.createContract(state);
            factory.createContract(state);
            StateViewModel stateVM = viewModelFactory.createStateViewModelFrom(state);

            if (i == 0) {
                automaton.setStartState(state);
                stateVM.setStartState(true);
            }

            viewModel.addViewModelElement(stateVM);
        }

        // Edges:
        List<State> states = new ArrayList<>(automaton.getStates());
        for (int i = 0; i < numberOfStates - 1; i++) {
            Edge edge = factory.createEdge(automaton, states.get(i), states.get(i+1));
            EdgeViewModel edgeVM = viewModelFactory.createEdgeViewModelFrom(edge);
            viewModel.addViewModelElement(edgeVM);
        }

        // Region:
        if (statesInRegion != 0) {
            Region region = factory.createRegion(automaton);
            for (int i = 0; i < statesInRegion; i++) {
                region.addState(states.get(i));
            }
            RegionViewModel regionVM = viewModelFactory.createRegionViewModelFrom(region);
            viewModel.addViewModelElement(regionVM);
        }
    }
}
