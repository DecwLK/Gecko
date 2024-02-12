package org.gecko.viewmodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.model.Element;
import org.gecko.model.GeckoModel;
import org.gecko.model.System;

/**
 * Represents the ViewModel component of a Gecko project, which connects the Model and View. Holds a
 * {@link ViewModelFactory} and a reference to the {@link GeckoModel}, as well as the current {@link EditorViewModel}
 * and a list of all opened {@link EditorViewModel}s. Maps all {@link PositionableViewModelElement}s to their
 * corresponding {@link Element}s from Model. Contains methods for managing the {@link EditorViewModel} and the retained
 * {@link PositionableViewModelElement}s.
 */
@Data
public class GeckoViewModel {
    @Getter(AccessLevel.NONE)
    private final HashMap<Element, PositionableViewModelElement<?>> modelToViewModel;
    private final GeckoModel geckoModel;
    private final ViewModelFactory viewModelFactory;
    private final Property<EditorViewModel> currentEditorProperty;
    private final SetProperty<EditorViewModel> openedEditorsProperty;
    @Getter
    private final ActionManager actionManager;

    public GeckoViewModel(GeckoModel geckoModel) {
        modelToViewModel = new HashMap<>();
        this.geckoModel = geckoModel;
        actionManager = new ActionManager(this);
        viewModelFactory = new ViewModelFactory(actionManager, this, geckoModel.getModelFactory());
        openedEditorsProperty = new SimpleSetProperty<>(FXCollections.observableSet());
        currentEditorProperty = new SimpleObjectProperty<>();

        // Create root system view model
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        switchEditor(rootSystemViewModel, false);
    }

    /**
     * Switches the current {@link EditorViewModel} to the one that contains the given {@link SystemViewModel} and has
     * the correct type (automaton  or system editor). If the {@link EditorViewModel} does not exist, a new one is
     * created.
     *
     * @param nextSystemViewModel the {@link SystemViewModel} that should be displayed in the editor
     * @param isAutomatonEditor   true if the editor should be an automaton editor, false if it should be a system
     *                            editor
     */
    public void switchEditor(SystemViewModel nextSystemViewModel, boolean isAutomatonEditor) {
        openedEditorsProperty.stream()
            .filter(editorViewModel -> (editorViewModel.getCurrentSystem().equals(nextSystemViewModel)
                && editorViewModel.isAutomatonEditor() == isAutomatonEditor))
            .findFirst()
            .ifPresentOrElse(this::setCurrentEditor,
                () -> setupNewEditorViewModel(nextSystemViewModel, isAutomatonEditor));
    }

    private void setupNewEditorViewModel(SystemViewModel nextSystemViewModel, boolean isAutomatonEditor) {
        SystemViewModel parent = null;
        if (nextSystemViewModel.getTarget().getParent() != null) {
            parent = (SystemViewModel) getViewModelElement(nextSystemViewModel.getTarget().getParent());
        }
        EditorViewModel editorViewModel =
            viewModelFactory.createEditorViewModel(nextSystemViewModel, parent, isAutomatonEditor);
        openedEditorsProperty.add(editorViewModel);
        setCurrentEditor(editorViewModel);
    }

    public PositionableViewModelElement<?> getViewModelElement(Element element) {
        return modelToViewModel.get(element);
    }

    public Set<PositionableViewModelElement<?>> getViewModelElements(Set<? extends Element> elements) {
        Set<PositionableViewModelElement<?>> positionableViewModelElements = new HashSet<>();
        elements.forEach(element -> positionableViewModelElements.add(getViewModelElement(element)));

        // Remove null elements TODO: THIS SHOULDN'T BE NECESSARY
        positionableViewModelElements.removeIf(Objects::isNull);

        return positionableViewModelElements;
    }

    /**
     * Adds a new {@link PositionableViewModelElement} to the {@link GeckoViewModel}. The element is mapped to its
     * corresponding {@link Element} from the model. The {@link PositionableViewModelElement} is then added to the
     * correct {@link EditorViewModel}.
     *
     * @param element the {@link PositionableViewModelElement} to add
     */
    public void addViewModelElement(PositionableViewModelElement<?> element) {
        modelToViewModel.put(element.getTarget(), element);
        updateEditors();
    }

    /**
     * Deletes a {@link PositionableViewModelElement} from the {@link GeckoViewModel}. The element is removed from the
     * mapping and from all {@link EditorViewModel}s. The selection managers of the editors are updated and the element
     * is removed from the editor that displays it.
     *
     * @param element the {@link PositionableViewModelElement} to delete
     */
    public void deleteViewModelElement(PositionableViewModelElement<?> element) {
        modelToViewModel.remove(element.getTarget());
        updateSelectionManagers(element);
        updateEditors();
    }

    private void updateSelectionManagers(PositionableViewModelElement<?> removedElement) {
        openedEditorsProperty.forEach(
            editorViewModel -> editorViewModel.getSelectionManager().updateSelections(removedElement));
    }

    private void updateEditors() {
        openedEditorsProperty.forEach(this::updateEditor);
        Set<EditorViewModel> editorViewModelsToDelete = openedEditorsProperty.stream()
            .filter(editorViewModel -> !modelToViewModel.containsValue(editorViewModel.getCurrentSystem()))
            .collect(Collectors.toSet());
        openedEditorsProperty.removeAll(editorViewModelsToDelete);
    }

    private void updateEditor(EditorViewModel editorViewModel) {
        editorViewModel.removePositionableViewModelElements(
            editorViewModel.getContainedPositionableViewModelElementsProperty()
                .stream()
                .filter(element -> !modelToViewModel.containsKey(element.getTarget()))
                .collect(Collectors.toSet()));

        addPositionableViewModelElementsToEditor(editorViewModel);
    }

    public EditorViewModel getCurrentEditor() {
        return currentEditorProperty.getValue();
    }

    private void setCurrentEditor(EditorViewModel editorViewModel) {
        currentEditorProperty.setValue(editorViewModel);
        updateEditors();
    }

    private void addPositionableViewModelElementsToEditor(EditorViewModel editorViewModel) {
        System currentSystem = editorViewModel.getCurrentSystem().getTarget();
        if (editorViewModel.isAutomatonEditor()) {
            editorViewModel.addPositionableViewModelElements(
                getViewModelElements(currentSystem.getAutomaton().getAllElements()));
        } else {
            editorViewModel.addPositionableViewModelElements(getViewModelElements(currentSystem.getAllElements()));
        }
    }

    public SystemViewModel getSystemViewModelWithPort(PortViewModel portViewModel) {
        System system = geckoModel.getSystemWithVariable(portViewModel.getTarget());
        if (system == null) {
            return null;
        }
        return (SystemViewModel) getViewModelElement(system);
    }
}
