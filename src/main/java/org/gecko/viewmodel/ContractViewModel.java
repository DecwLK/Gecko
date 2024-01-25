package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.Contract;

/**
 * Represents an abstraction of a {@link Contract} model element. A {@link ContractViewModel} is described by a name, a
 * pre- and a postcondition. Contains methods for managing the afferent data and updating the target-{@link Contract}.
 */
@Getter
@Setter
public class ContractViewModel extends AbstractViewModelElement<Contract> implements Renamable {

    private final StringProperty nameProperty;
    private final StringProperty preConditionProperty;
    private final StringProperty postConditionProperty;

    public ContractViewModel(int id, Contract target) {
        super(id, target);
        this.nameProperty = new SimpleStringProperty();
        this.preConditionProperty = new SimpleStringProperty(target.getPreCondition().getCondition());
        this.postConditionProperty = new SimpleStringProperty(target.getPreCondition().getCondition());
        setName(target.getName());
    }

    public void setPrecondition(@NonNull String precondition) {
        preConditionProperty.setValue(precondition);
    }

    public void setPostcondition(@NonNull String postcondition) {
        postConditionProperty.setValue(postcondition);
    }

    public String getPrecondition() {
        return preConditionProperty.getValue();
    }

    public String getPostcondition() {
        return postConditionProperty.getValue();
    }

    @Override
    public void updateTarget() {
        target.setName(getName());
        target.getPreCondition().setCondition(getPrecondition());
        target.getPostCondition().setCondition(getPostcondition());
    }

    @Override
    public String getName() {
        return nameProperty.getValue();
    }

    @Override
    public void setName(String name) {
        nameProperty.setValue(name);
    }
}