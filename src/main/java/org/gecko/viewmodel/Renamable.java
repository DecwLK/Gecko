package org.gecko.viewmodel;

import javafx.beans.property.StringProperty;

public interface Renamable {
    String getName();

    void setName(String name);

    StringProperty getNameProperty(); // TODO: inspector needs this for name fields
}
