package org.gecko.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.gecko.model.Automaton;
import org.gecko.model.Element;
import org.gecko.model.System;
import org.gecko.model.GeckoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                             .filter(editorViewModel -> (editorViewModel.getCurrentSystem() == nextSystemViewModel &&
                                 editorViewModel.isAutomatonEditor() == isAutomatonEditor))
                             .findFirst()
                             .ifPresentOrElse(currentEditorProperty::setValue, () -> {
                                 EditorViewModel editorViewModel = viewModelFactory.createEditorViewModel(nextSystemViewModel, isAutomatonEditor);
                                 openedEditorsProperty.add(editorViewModel);
                                 currentEditorProperty.setValue(editorViewModel);
                                 initializeEditorWithPositionableViewModelElements(editorViewModel, nextSystemViewModel);
                             });
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
    }

    public void deleteViewModelElement(PositionableViewModelElement<?> element) {
        modelToViewModel.remove(element.getTarget());
    }

    public EditorViewModel getCurrentEditor() {
        return currentEditorProperty.getValue();
    }

    private void initializeEditorWithPositionableViewModelElements(EditorViewModel editorViewModel, SystemViewModel systemViewModel) {
        if (editorViewModel.isAutomatonEditor()) {
            Automaton automaton = systemViewModel.getTarget().getAutomaton();
            editorViewModel.addPositionableViewModelElements(getViewModelElements(automaton.getStates()));
            editorViewModel.addPositionableViewModelElements(getViewModelElements(automaton.getEdges()));
            editorViewModel.addPositionableViewModelElements(getViewModelElements(automaton.getRegions()));
        } else {
            System system = systemViewModel.getTarget();
            editorViewModel.addPositionableViewModelElements(getViewModelElements(system.getChildren()));
            editorViewModel.addPositionableViewModelElements(getViewModelElements(system.getVariables()));
            editorViewModel.addPositionableViewModelElements(getViewModelElements(system.getConnections()));
        }
    }

}
