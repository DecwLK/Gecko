package org.gecko.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableObjectValue;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Element;
import org.gecko.model.GeckoModel;

import java.util.HashMap;

@Getter @Setter
public class GeckoViewModel {
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
}
