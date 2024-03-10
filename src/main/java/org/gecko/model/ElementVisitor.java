package org.gecko.model;

import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;

/**
 * Represents a visitor pattern for performing operations on {@link Element}s. Concrete visitors must implement this
 * interface to define specific behavior for each {@link Element}.
 */
public interface ElementVisitor {
    void visit(State state) throws ModelException;

    void visit(Contract contract);

    void visit(SystemConnection systemConnection) throws ModelException, MissingViewModelElementException;

    void visit(Variable variable) throws ModelException;

    void visit(System system) throws ModelException;

    void visit(Region region) throws ModelException, MissingViewModelElementException;

    void visit(Edge edge) throws ModelException, MissingViewModelElementException;
}
