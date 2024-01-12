package org.gecko.viewmodel;

import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Contract;

public class ContractViewModel<T extends Contract> extends AbstractViewModelElement<T> implements Renamable {
    private StringProperty name;

    @Getter @Setter private StringProperty preCondition;

    @Getter @Setter private StringProperty postCondition;

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

    @Override
    public void setName(String name) {
        // TODO
    }
}
