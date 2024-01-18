package org.gecko.viewmodel;

/**
 * Provides methods for renamable objects, that is view model elements with a name property.
 * These include retrieving and modifying the name of the view model element.
 */
public interface Renamable {
    String getName();

    void setName(String name);
}
