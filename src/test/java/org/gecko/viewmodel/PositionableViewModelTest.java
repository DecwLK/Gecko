/*
 * Copyright (c) 2024.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gecko.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.gecko.model.GeckoModel;
import org.gecko.model.ModelFactory;
import org.gecko.util.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionableViewModelTest {
    GeckoViewModel geckoViewModel;
    GeckoModel geckoModel;
    ViewModelFactory viewModelFactory;
    ModelFactory modelFactory;
    SystemViewModel root;
    StateViewModel stateViewModel1;
    StateViewModel stateViewModel2;

    @BeforeEach
    void setUp() throws ModelException {
        geckoViewModel = TestHelper.createGeckoViewModel();
        geckoModel = geckoViewModel.getGeckoModel();
        viewModelFactory = geckoViewModel.getViewModelFactory();
        modelFactory = geckoModel.getModelFactory();
        root = geckoViewModel.getCurrentEditor().getCurrentSystem();
        try {
            stateViewModel1 = viewModelFactory.createStateViewModelIn(root);
            stateViewModel2 = viewModelFactory.createStateViewModelIn(root);
        } catch (ModelException e) {
            fail();
        }
    }

    @Test
    void edgeViewModelTest() throws ModelException {
        EdgeViewModel edgeViewModel = viewModelFactory.createEdgeViewModelIn(root, stateViewModel1, stateViewModel2);
        assertEquals(stateViewModel1, edgeViewModel.getSource());
        assertEquals(stateViewModel2, edgeViewModel.getDestination());
        assertEquals(stateViewModel1.getIncomingEdges().size(), 0);
        assertEquals(stateViewModel1.getOutgoingEdges().size(), 1);
        assertEquals(stateViewModel2.getIncomingEdges().size(), 1);
        assertEquals(stateViewModel2.getOutgoingEdges().size(), 0);

        ContractViewModel contractViewModel = viewModelFactory.createContractViewModelIn(stateViewModel1);
        edgeViewModel.setContract(contractViewModel);
        assertEquals(contractViewModel, edgeViewModel.getContract());
        assertEquals(stateViewModel1.getContracts().size(), 1);
    }
}
