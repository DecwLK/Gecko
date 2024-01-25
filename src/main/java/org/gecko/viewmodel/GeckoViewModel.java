package org.gecko.viewmodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.model.CreateElementVisitor;
import org.gecko.model.DeleteElementVisitor;
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
    private final ListProperty<EditorViewModel> openedEditorsProperty;
    @Getter
    private final ActionManager actionManager;

    public GeckoViewModel(GeckoModel geckoModel) {
        modelToViewModel = new HashMap<>();
        this.geckoModel = geckoModel;
        actionManager = new ActionManager(this);
        viewModelFactory = new ViewModelFactory(actionManager, this, geckoModel.getModelFactory());
        openedEditorsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        currentEditorProperty = new SimpleObjectProperty<>();

        // Create root system view model
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        switchEditor(rootSystemViewModel, false);
    }

    public void switchEditor(SystemViewModel nextSystemViewModel, boolean isAutomatonEditor) {
        openedEditorsProperty.stream()
            .filter(editorViewModel -> (editorViewModel.getCurrentSystem() == nextSystemViewModel
                && editorViewModel.isAutomatonEditor() == isAutomatonEditor))
            .findFirst()
            .ifPresentOrElse(this::setCurrentEditor,
                () -> setupNewEditorViewModel(nextSystemViewModel, isAutomatonEditor));
    }

    private void setupNewEditorViewModel(SystemViewModel nextSystemViewModel, boolean isAutomatonEditor) {
        SystemViewModel currentSystemViewModel = null;
        if (getCurrentEditor() != null) {
            currentSystemViewModel = getCurrentEditor().getCurrentSystem();
        }

        EditorViewModel editorViewModel = viewModelFactory.createEditorViewModel(nextSystemViewModel,
            (nextSystemViewModel == currentSystemViewModel) ? null : currentSystemViewModel, isAutomatonEditor);
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
        positionableViewModelElements.removeIf(element -> element == null);

        return positionableViewModelElements;
    }

    public void addViewModelElement(PositionableViewModelElement<?> element) {
        modelToViewModel.put(element.getTarget(), element);
        updateCurrentEditor();
    }

    /**
     * In addition to adding the positionable view model element to the view model, this method also adds the
     * positionable view model element to the model.
     *
     * @param element The element to add.
     */
    public void restoreViewModelElement(PositionableViewModelElement<?> element) {
        // Add to model
        CreateElementVisitor createElementVisitor =
            new CreateElementVisitor(getCurrentEditor().getCurrentSystem().getTarget());
        element.getTarget().accept(createElementVisitor);

        addViewModelElement(element);
    }

    public void deleteViewModelElement(PositionableViewModelElement<?> element) {
        // Remove from view model hashmap
        modelToViewModel.remove(element.getTarget());

        // Remove from model
        DeleteElementVisitor deleteElementVisitor =
            new DeleteElementVisitor(getCurrentEditor().getCurrentSystem().getTarget());
        element.getTarget().accept(deleteElementVisitor);

        updateCurrentEditor();
    }

    private void updateCurrentEditor() {
        EditorViewModel currentEditor = getCurrentEditor();
        if (currentEditor == null) {
            return;
        }

        currentEditor.removePositionableViewModelElements(
            currentEditor.getContainedPositionableViewModelElementsProperty()
                .stream()
                .filter(element -> !modelToViewModel.containsKey(element.getTarget()))
                .collect(Collectors.toSet()));

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
            editorViewModel.addPositionableViewModelElements(
                getViewModelElements(currentSystem.getAutomaton().getAllElements()));
        } else {
            editorViewModel.addPositionableViewModelElements(getViewModelElements(currentSystem.getAllElements()));
        }
    }

}
