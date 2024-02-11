/**
 * Clusters classes which manage the {@link org.gecko.view.views.EditorView}, respectively what is displayed in the
 * current view of Gecko. The {@link org.gecko.view.views.ViewFactory} and the
 * {@link org.gecko.view.views.ViewElementCreatorVisitor} are responsible for creating
 * {@link org.gecko.view.views.viewelement.ViewElement ViewElements}, while the
 * {@link org.gecko.view.views.ViewElementSearchVisitor} collects view model element matches to a search based on the
 * name of the elements. The {@link org.gecko.view.views.FloatingUIBuilder} builds floating UI elements (buttons and
 * labels) displayed in the view.
 */
package org.gecko.view.views;