package org.gecko.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableObjectValue;
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
    private ObservableObjectValue<EditorViewModel> currentEditor;
    private final ListProperty<EditorViewModel> openedEditors;

    public GeckoViewModel(GeckoModel geckoModel) {
        modelToViewModel = new HashMap<>();
        this.geckoModel = geckoModel;
        viewModelFactory = new ViewModelFactory(this, geckoModel.getModelFactory());
        openedEditors = new SimpleListProperty<>();
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
}
