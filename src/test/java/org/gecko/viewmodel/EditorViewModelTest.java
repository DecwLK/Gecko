package org.gecko.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import javafx.collections.ObservableList;
import org.gecko.exceptions.ModelException;
import org.gecko.model.GeckoModel;
import org.gecko.tools.ToolType;
import org.junit.jupiter.api.Test;

class EditorViewModelTest {

    @Test
    void getRegionViewModels() throws ModelException {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        RegionViewModel regionViewModel2 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        RegionViewModel regionViewModel1 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel = null;
        try {
            stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        } catch (ModelException e) {
            fail();
        }
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        EditorViewModel editorViewModel = geckoViewModel.getCurrentEditor();

        regionViewModel1.getTarget().addState(stateViewModel.getTarget());
        assertEquals(editorViewModel.getRegionViewModels(stateViewModel), List.of(regionViewModel1));

        regionViewModel2.getTarget().addState(stateViewModel.getTarget());
        assertTrue(editorViewModel.getRegionViewModels(stateViewModel)
            .containsAll(List.of(regionViewModel1, regionViewModel2)));
    }

    @Test
    void updateRegionViewModels() throws ModelException {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        RegionViewModel regionViewModel = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel = null;
        try {
            stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        } catch (ModelException e) {
            fail();
        }
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        EditorViewModel editorViewModel = geckoViewModel.getCurrentEditor();

        stateViewModel.setCenter(regionViewModel.getCenter());
        editorViewModel.updateRegions();
        assertTrue(regionViewModel.getTarget().getStates().contains(stateViewModel.getTarget()));

        ObservableList<RegionViewModel> regionViewModels = editorViewModel.getRegionViewModels(stateViewModel);
        assertTrue(regionViewModels.contains(regionViewModel));

        stateViewModel.setCenter(regionViewModel.getCenter().add(1000, 1000));
        editorViewModel.updateRegions();
        assertFalse(regionViewModels.contains(regionViewModel));

        stateViewModel.setCenter(regionViewModel.getCenter());
        editorViewModel.updateRegions();
        assertTrue(regionViewModels.contains(regionViewModel));
    }

    @Test
    void testToolSelections() throws ModelException {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        RegionViewModel regionViewModel = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);

        geckoViewModel.switchEditor(rootSystemViewModel, true);
        EditorViewModel editorViewModel = geckoViewModel.getCurrentEditor();

        assertEquals(editorViewModel.getCurrentTool().getToolType(), ToolType.CURSOR);
        editorViewModel.setCurrentTool(ToolType.STATE_CREATOR);
        assertEquals(editorViewModel.getCurrentToolType(), ToolType.STATE_CREATOR);
    }

    @Test
    void testPositionableViewModelElements() throws ModelException {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        RegionViewModel regionViewModel = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel = null;
        try {
            stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        } catch (ModelException e) {
            fail();
        }
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        EditorViewModel editorViewModel = geckoViewModel.getCurrentEditor();
        assertEquals(stateViewModel.getName(), "Element_3");
        assertEquals(editorViewModel.getElementsByName("Element_3").size(), 1);

        assertTrue(editorViewModel.getPositionableViewModelElements().contains(stateViewModel));
        assertTrue(editorViewModel.getContainedPositionableViewModelElementsProperty().contains(regionViewModel));

        editorViewModel.getSelectionManager().select(stateViewModel);
        assertEquals(editorViewModel.getFocusedElement(), stateViewModel);
    }
}