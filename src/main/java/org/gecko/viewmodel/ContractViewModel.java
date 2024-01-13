package org.gecko.viewmodel;

import javafx.beans.property.StringProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Contract;

public class ContractViewModel extends AbstractViewModelElement<Contract> implements Renamable {
    private StringProperty name;

    @Getter @Setter private StringProperty preCondition;

    @Getter @Setter private StringProperty postCondition;

    public ContractViewModel(Contract target) {
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
