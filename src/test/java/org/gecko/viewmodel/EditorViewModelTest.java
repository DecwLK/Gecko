package org.gecko.viewmodel;

import org.gecko.model.GeckoModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EditorViewModelTest {

    @Test
    void getRegionViewModels() {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        RegionViewModel regionViewModel2 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        RegionViewModel regionViewModel1 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        EditorViewModel editorViewModel = geckoViewModel.getCurrentEditor();

        regionViewModel1.getTarget().addState(stateViewModel.getTarget());
        assertEquals(editorViewModel.getRegionViewModels(stateViewModel), List.of(regionViewModel1));

        regionViewModel2.getTarget().addState(stateViewModel.getTarget());
        assertTrue(editorViewModel.getRegionViewModels(stateViewModel).containsAll(List.of(regionViewModel1, regionViewModel2)));
    }
}