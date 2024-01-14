package org.gecko.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.gecko.model.Element;
import org.gecko.model.GeckoModel;

import java.util.HashMap;

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
        openedEditorsProperty = new SimpleListProperty<>();
        currentEditorProperty = new SimpleObjectProperty<>();
    }

    public void switchEditor(SystemViewModel nextSystemViewModel, boolean isAutomatonEditor) {
        //TODO stub
    }

    public PositionableViewModelElement<?> getViewModelElement(Element element) {
        //TODO stub
        return null;
    }

    public void addViewModelElement(PositionableViewModelElement<?> element) {
        //TODO stub
    }

    public void deleteViewModelElement(PositionableViewModelElement<?> element) {
        //TODO stub
    }

    public EditorViewModel getCurrentEditor() {
        return currentEditorProperty.getValue();
    }
}
