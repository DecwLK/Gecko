/**
 * Contains the {@link org.gecko.model.GeckoModel} and groups all classes that represent {@link org.gecko.model.Element
 * Elements} from the domain model of Gecko, of which some are {@link org.gecko.model.Renamable}.
 * Gecko's model has a tree-structure made up of {@link org.gecko.model.System Systems}. These have
 * {@link org.gecko.model.Variable Variables} which can be connected through {@link org.gecko.model.SystemConnection
 * SystemConnections} if they are found in systems on the same level, and every system has a corresponding {@link
 * org.gecko.model.Automaton}, which consists of {@link org.gecko.model.State States} connected through
 * {@link org.gecko.model.Edge Edges} and grouped in {@link org.gecko.model.Region Regions}. The
 * {@link org.gecko.model.ModelFactory} is responsible for creating instances of each of these elements and the
 * {@link org.gecko.model.ElementVisitor} allows access to them.
 */
package org.gecko.model;