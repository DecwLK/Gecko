package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.gecko.model.Element;
import org.gecko.model.GeckoModel;
import org.gecko.model.System;

@Data
public class GeckoViewModel {
    @Getter(AccessLevel.NONE)
    private final HashMap<Element, PositionableViewModelElement<?>> modelToViewModel;
    private final GeckoModel geckoModel;
    private final ViewModelFactory viewModelFactory;
    private final Property<EditorViewModel> currentEditorProperty;
    private final ListProperty<EditorViewModel> openedEditorsProperty;

    public GeckoViewModel(GeckoModel geckoModel) {
        modelToViewModel = new HashMap<>();
        this.geckoModel = geckoModel;
        viewModelFactory = new ViewModelFactory(this, geckoModel.getModelFactory());
        openedEditorsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        currentEditorProperty = new SimpleObjectProperty<>();
    }

    public void switchEditor(SystemViewModel nextSystemViewModel, boolean isAutomatonEditor) {
        openedEditorsProperty.stream()
                             .filter(editorViewModel -> (editorViewModel.getCurrentSystem() == nextSystemViewModel
                                 && editorViewModel.isAutomatonEditor() == isAutomatonEditor))
                             .findFirst()
                             .ifPresentOrElse(this::setCurrentEditor, () -> setupNewEditorViewModel(nextSystemViewModel, isAutomatonEditor));
    }

    private void setupNewEditorViewModel(SystemViewModel nextSystemViewModel, boolean isAutomatonEditor) {
        EditorViewModel editorViewModel =
            viewModelFactory.createEditorViewModel(nextSystemViewModel, getCurrentEditor().getCurrentSystem(), isAutomatonEditor);
        openedEditorsProperty.add(editorViewModel);
        setCurrentEditor(editorViewModel);
    }

    public PositionableViewModelElement<?> getViewModelElement(Element element) {
        return modelToViewModel.get(element);
    }

    public List<PositionableViewModelElement<?>> getViewModelElements(List<? extends Element> elements) {
        List<PositionableViewModelElement<?>> positionableViewModelElements = new ArrayList<>();
        elements.forEach(element -> positionableViewModelElements.add(getViewModelElement(element)));
        return positionableViewModelElements;
    }

    public void addViewModelElement(PositionableViewModelElement<?> element) {
        modelToViewModel.put(element.getTarget(), element);
        updateCurrentEditor();
    }

    public void deleteViewModelElement(PositionableViewModelElement<?> element) {
        modelToViewModel.remove(element.getTarget());
        updateCurrentEditor();
    }

    private void updateCurrentEditor() {
        EditorViewModel currentEditor = getCurrentEditor();
        if (currentEditor == null) {
            return;
        }

        currentEditor.removePositionableViewModelElements(currentEditor.getContainedPositionableViewModelElementsProperty()
                                                                       .stream()
                                                                       .filter(element -> !modelToViewModel.containsKey(element.getTarget()))
                                                                       .toList());
        addPositionableViewModelElementsToEditor(currentEditor);
    }

    public EditorViewModel getCurrentEditor() {
        return currentEditorProperty.getValue();
    }

    private void setCurrentEditor(EditorViewModel editorViewModel) {
        currentEditorProperty.setValue(editorViewModel);
        updateCurrentEditor();
    }

    private void addPositionableViewModelElementsToEditor(EditorViewModel editorViewModel) {
        System currentSystem = editorViewModel.getCurrentSystem().getTarget();
        if (editorViewModel.isAutomatonEditor()) {
            editorViewModel.addPositionableViewModelElements(getViewModelElements(currentSystem.getAutomaton().getAllElements()));
        } else {
            editorViewModel.addPositionableViewModelElements(getViewModelElements(currentSystem.getAllElements()));
        }
    }

}
