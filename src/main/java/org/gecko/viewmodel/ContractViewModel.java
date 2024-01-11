package org.gecko.viewmodel;

import javafx.beans.property.StringProperty;
import org.gecko.model.Element;

// TODO: Extend T with Contract.
public class ContractViewModel<T extends Element> extends AbstractViewModelElement<T> implements Renamable {
    private StringProperty name;
    private StringProperty preCondition;
    private StringProperty postCondition;

    public ContractViewModel(T target) {
        super(target);
    }

    @Override
    public void updateTarget() {
        // TODO
    }

    @Override
    public String getName() {
        // TODO
        return null;
    }

    public void setName(String name) {
        // TODO
    }
}
