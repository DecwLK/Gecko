package org.gecko.viewmodel;

import javafx.beans.property.Property;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

@Setter @Getter
public class PortViewModel<T extends Variable> extends BlockViewModelElement<T> {
    private Property<Visibility> visibility;

    public PortViewModel(T target) {
        super(target);
    }
}
