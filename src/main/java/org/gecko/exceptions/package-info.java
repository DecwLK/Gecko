/**
 * Contains custom {@link java.lang.Exception Exceptions} used in Gecko to signalize errors or faulty interactions.
 * A {@link org.gecko.exceptions.MissingViewModelElementException} is thrown during the creation of view model elements
 * that have missing dependencies and cannot be created. A {@link org.gecko.exceptions.ModelException} is thrown during
 * the creation of model elements or while changing their properties, if the provided values are invalid.
 */
package org.gecko.exceptions;