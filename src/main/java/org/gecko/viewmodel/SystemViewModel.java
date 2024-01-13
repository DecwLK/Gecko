package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.System;

@Getter @Setter
public class SystemViewModel extends BlockViewModelElement<System> {
   private StringProperty code;
   private ObservableList<PortViewModel> ports;

    public SystemViewModel(System target) {
        super(target);
        super.setName(target.getName());
        this.code = new SimpleStringProperty(target.getCode());
        this.ports = FXCollections.observableArrayList();
    }

    @Override
    public void updateTarget() {
        // TODO
    }

    @Override
    public void accept(PositionableViewModelElementVisitor visitor) {
        visitor.visit(this);
    }
}
