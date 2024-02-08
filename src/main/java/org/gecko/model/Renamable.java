package org.gecko.model;

import org.gecko.exceptions.ModelException;

/**
 * Provides methods for renamable objects, that is model elements with a name attribute. These include retrieving and
 * modifying the name of the model element.
 */
public interface Renamable {
    String getName();

    void setName(String name) throws ModelException;
}
