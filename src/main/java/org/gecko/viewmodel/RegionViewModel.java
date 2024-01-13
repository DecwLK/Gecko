package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import javafx.scene.paint.Color;
import org.gecko.model.Region;

import java.util.List;

@Getter @Setter
public class RegionViewModel extends BlockViewModelElement<Region> {
    private Property<Color> color;
    private ContractViewModel contract;
    private StringProperty invariant;
    private List<StateViewModel> states;

    public RegionViewModel(Region target) {
        super(target);
    }

    public void addState(StateViewModel state) {
        // TODO
    }
}
