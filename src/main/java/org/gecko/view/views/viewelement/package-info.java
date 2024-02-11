/**
 * Clusters all classes that represent {@link org.gecko.view.views.viewelement.ViewElement ViewElements}, which are
 * bound to the view model elements of Gecko. These are divided in
 * {@link org.gecko.view.views.viewelement.BlockViewElement BlockViewElements} and
 * {@link org.gecko.view.views.viewelement.ConnectionViewElement ConnectionViewElements} and can be drawn in the view.
 * There exists a correspondent view element for each of the view model elements, except for
 * {@link org.gecko.viewmodel.ContractViewModel ContractViewModel}, which is not independently displayed in the view.
 * The {@link org.gecko.view.views.viewelement.ViewElementVisitor} allows access to each of the view elements.
 */
package org.gecko.view.views.viewelement;