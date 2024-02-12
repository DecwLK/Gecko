package org.gecko.view.inspector.element.list;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.RegionViewModel;

/**
 * A concrete representation of an {@link AbstractInspectorList} encapsulating an {@link InspectorLabel}.
 */
public class InspectorRegionList extends AbstractInspectorList<InspectorLabel> {
    private static final double PREF_HEIGHT = 100;

    public InspectorRegionList(ObservableList<RegionViewModel> regionViewModelList) {
        for (RegionViewModel regionViewModel : regionViewModelList) {
            InspectorLabel regionLabel = new InspectorLabel(regionViewModel.getName());
            getItems().add(regionLabel);
        }

        regionViewModelList.addListener(this::updateRegionList);
        setPrefHeight(PREF_HEIGHT);
    }

    private void updateRegionList(ListChangeListener.Change<? extends RegionViewModel> c) {
        while (c.next()) {
            if (c.wasAdded()) {
                for (RegionViewModel regionViewModel : c.getAddedSubList()) {
                    if (findRegionLabel(regionViewModel.getName()) != null) {
                        continue;
                    }
                    InspectorLabel regionLabel = new InspectorLabel(regionViewModel.getName());
                    getItems().add(regionLabel);
                }
            } else if (c.wasRemoved()) {
                for (RegionViewModel regionViewModel : c.getRemoved()) {
                    InspectorLabel regionLabel = findRegionLabel(regionViewModel.getName());
                    getItems().remove(regionLabel);
                }
            }
        }
    }

    private InspectorLabel findRegionLabel(String name) {
        return getItems().stream().filter(regionLabel -> regionLabel.getText().equals(name)).findFirst().orElse(null);
    }
}
