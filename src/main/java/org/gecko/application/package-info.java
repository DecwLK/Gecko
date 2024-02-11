/**
 * The contained classes hold together the data and functionalities provided in the other packages of Gecko.
 * {@link org.gecko.application.Main} provides the execution of the application and instantiates an initial
 * {@link org.gecko.application.Gecko}, which holds a {@link org.gecko.model.GeckoModel GeckoModel}, a
 * {@link org.gecko.view.GeckoView GeckoView} and a {@link org.gecko.viewmodel.GeckoViewModel GeckoViewModel} and is
 * managed by the {@link org.gecko.application.GeckoManager}. The creation and modification of files afferent to Gecko
 * projects is also managed at this level by the {@link org.gecko.application.GeckoIOManager}.
 */
package org.gecko.application;