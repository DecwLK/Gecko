package org.gecko.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.GeckoModel;
import org.gecko.model.ModelFactory;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.Variable;
import org.gecko.util.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewModelFactoryTest {
    GeckoViewModel geckoViewModel;
    GeckoModel geckoModel;
    ViewModelFactory viewModelFactory;
    ModelFactory modelFactory;
    SystemViewModel root;
    SystemViewModel systemViewModel1;
    SystemViewModel systemViewModel2;
    SystemViewModel systemViewModel11;
    StateViewModel stateViewModel1;
    StateViewModel stateViewModel2;

    @BeforeEach
    void setUp() {
        geckoViewModel = TestHelper.createGeckoViewModel();
        geckoModel = geckoViewModel.getGeckoModel();
        viewModelFactory = geckoViewModel.getViewModelFactory();
        modelFactory = geckoModel.getModelFactory();
        root = geckoViewModel.getCurrentEditor().getCurrentSystem();
        systemViewModel1 = viewModelFactory.createSystemViewModelIn(root);
        systemViewModel2 = viewModelFactory.createSystemViewModelIn(root);
        systemViewModel11 = viewModelFactory.createSystemViewModelIn(systemViewModel1);
        try {
            stateViewModel1 = viewModelFactory.createStateViewModelIn(systemViewModel1);
            stateViewModel2 = viewModelFactory.createStateViewModelIn(systemViewModel1);
        } catch (ModelException e) {
            fail();
        }
    }

    @Test
    void testModelStructure() {
        assertTrue(root.getTarget().getChildren().contains(systemViewModel1.getTarget()));
        assertTrue(systemViewModel1.getTarget().getChildren().contains(systemViewModel11.getTarget()));
    }

    @Test
    void testAddPorts() {
        PortViewModel portViewModel1 = viewModelFactory.createPortViewModelIn(systemViewModel1);
        PortViewModel portViewModel2 = viewModelFactory.createPortViewModelIn(systemViewModel1);
        assertTrue(systemViewModel1.getTarget().getVariables().contains(portViewModel1.getTarget()));
        assertTrue(systemViewModel1.getTarget().getVariables().contains(portViewModel2.getTarget()));
        assertTrue(systemViewModel1.getPortsProperty().contains(portViewModel1));
        assertTrue(systemViewModel1.getPortsProperty().contains(portViewModel2));
    }

    @Test
    void testModelToViewModel() {
        assertEquals(systemViewModel1, geckoViewModel.getViewModelElement(systemViewModel1.getTarget()));
        assertEquals(systemViewModel11, geckoViewModel.getViewModelElement(systemViewModel11.getTarget()));
    }

    @Test
    void testAddSystemConnectionBetweenPorts() {
        PortViewModel portViewModel1 = viewModelFactory.createPortViewModelIn(systemViewModel1);
        PortViewModel portViewModel2 = viewModelFactory.createPortViewModelIn(systemViewModel2);
        SystemConnectionViewModel systemConnectionViewModel = null;
        try {
            systemConnectionViewModel =
                viewModelFactory.createSystemConnectionViewModelIn(root, portViewModel1, portViewModel2);
        } catch (ModelException e) {
            fail();
        }
        assertTrue(root.getTarget().getConnections().contains(systemConnectionViewModel.getTarget()));
        assertEquals(systemConnectionViewModel.getTarget().getSource(), portViewModel1.getTarget());
        assertEquals(systemConnectionViewModel.getTarget().getDestination(), portViewModel2.getTarget());
        assertEquals(systemConnectionViewModel.getSource(), portViewModel1);
        assertEquals(systemConnectionViewModel.getDestination(), portViewModel2);
    }

    @Test
    void testStatesInSystem() {
        assertTrue(systemViewModel1.getTarget().getAutomaton().getStates().contains(stateViewModel1.getTarget()));
        assertTrue(systemViewModel1.getTarget().getAutomaton().getStates().contains(stateViewModel2.getTarget()));
        assertNotNull(geckoViewModel.getViewModelElement(stateViewModel1.getTarget()));
        assertNotNull(geckoViewModel.getViewModelElement(stateViewModel2.getTarget()));
    }

    @Test
    void testAddContractsToState() {
        ContractViewModel contractViewModel1 = viewModelFactory.createContractViewModelIn(stateViewModel1);
        ContractViewModel contractViewModel2 = viewModelFactory.createContractViewModelIn(stateViewModel1);
        assertTrue(stateViewModel1.getTarget().getContracts().contains(contractViewModel1.getTarget()));
        assertTrue(stateViewModel1.getTarget().getContracts().contains(contractViewModel2.getTarget()));
        assertTrue(stateViewModel1.getContractsProperty().contains(contractViewModel1));
        assertTrue(stateViewModel1.getContractsProperty().contains(contractViewModel2));
    }

    @Test
    void testAddEdgesToSystem() {
        EdgeViewModel edgeViewModel1 = viewModelFactory.createEdgeViewModelIn(root, stateViewModel1, stateViewModel2);
        EdgeViewModel edgeViewModel2 = viewModelFactory.createEdgeViewModelIn(root, stateViewModel1, stateViewModel1);
        assertTrue(root.getTarget().getAutomaton().getEdges().contains(edgeViewModel1.getTarget()));
        assertTrue(root.getTarget().getAutomaton().getEdges().contains(edgeViewModel2.getTarget()));
        assertEquals(stateViewModel2, edgeViewModel1.getDestination());
        assertEquals(stateViewModel1, edgeViewModel2.getDestination());
        assertEquals(stateViewModel1, edgeViewModel1.getSource());
        assertEquals(stateViewModel1, edgeViewModel2.getSource());
    }

    @Test
    void testCreateStateFromModelWithContracts() {
        State state = modelFactory.createState(systemViewModel1.getTarget().getAutomaton());
        Contract contract1 = modelFactory.createContract(state);
        contract1.setName("contract1");
        Contract contract2 = modelFactory.createContract(state);
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelFrom(state);
        assertTrue(stateViewModel.getTarget().getContracts().contains(contract1));
        assertTrue(stateViewModel.getTarget().getContracts().contains(contract2));
        assertEquals(2, stateViewModel.getContractsProperty().size());
        assertEquals("contract1", stateViewModel.getContractsProperty().getFirst().getName());
    }

    @Test
    void testCreateEdgeFromModelFail() {
        State source = modelFactory.createState(systemViewModel1.getTarget().getAutomaton());
        State destination = modelFactory.createState(systemViewModel1.getTarget().getAutomaton());
        Edge edge = modelFactory.createEdge(systemViewModel1.getTarget().getAutomaton(), source, destination);
        assertThrows(MissingViewModelElementException.class, () -> viewModelFactory.createEdgeViewModelFrom(edge));
    }

    @Test
    void testCreateEdgeFromModel() {
        Edge edge = modelFactory.createEdge(systemViewModel1.getTarget().getAutomaton(), stateViewModel1.getTarget(),
            stateViewModel2.getTarget());
        try {
            EdgeViewModel edgeViewModel = viewModelFactory.createEdgeViewModelFrom(edge);
            assertEquals(edge, edgeViewModel.getTarget());
            assertEquals(stateViewModel1, edgeViewModel.getSource());
            assertEquals(stateViewModel2, edgeViewModel.getDestination());
        } catch (MissingViewModelElementException e) {
            fail();
        }
    }

    @Test
    void testCreateRegionFromModel() {
        Region region = modelFactory.createRegion(systemViewModel1.getTarget().getAutomaton());
        region.addState(stateViewModel1.getTarget());
        region.addState(stateViewModel2.getTarget());
        try {
            RegionViewModel regionViewModel = viewModelFactory.createRegionViewModelFrom(region);
            assertEquals(region, regionViewModel.getTarget());
            assertEquals(2, regionViewModel.getStatesProperty().size());
            assertTrue(regionViewModel.getStatesProperty().contains(stateViewModel1));
            assertTrue(regionViewModel.getStatesProperty().contains(stateViewModel2));
        } catch (MissingViewModelElementException e) {
            fail();
        }
    }

    @Test
    void testCreateSystemFromModel() {
        System system = modelFactory.createSystem(root.getTarget());
        Variable variable1 = modelFactory.createVariable(system);
        Variable variable2 = modelFactory.createVariable(system);
        system.addVariables(Set.of(variable1, variable2));
        SystemViewModel systemViewModel = viewModelFactory.createSystemViewModelFrom(system);
        assertNotNull(geckoViewModel.getViewModelElement(variable1));
        assertNotNull(geckoViewModel.getViewModelElement(variable2));
        assertEquals(2, systemViewModel.getPortsProperty().size());
    }
}