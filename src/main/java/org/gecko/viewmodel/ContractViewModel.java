package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Contract;

public class ContractViewModel extends AbstractViewModelElement<Contract> implements Renamable {
    private StringProperty name;

    @Getter @Setter private StringProperty preCondition;

    @Getter @Setter private StringProperty postCondition;

    public ContractViewModel(Contract target) {
        super(target);
        this.name = new SimpleStringProperty(target.getName());
        this.preCondition = new SimpleStringProperty(target.getPreCondition().getCondition());
        this.postCondition = new SimpleStringProperty(target.getPreCondition().getCondition());
    }

    @Override
    public void updateTarget() {
        // TODO
    }

    @Override
    public String getName() {
        return this.name.getValue();
    }

    @Override
    public void setName(String name) {
        // TODO: further checks before updating?
        this.name.setValue(name);
    }
}