package org.gecko.exceptions;

/** A custom {@link Exception} for the Gecko Graphic Editor for Contract Automata. */
public class MissingViewModelElementException extends GeckoException {
    public MissingViewModelElementException(String message) {
        super(message);
    }
}
