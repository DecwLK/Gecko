/**
 * Contains the {@link org.gecko.viewmodel.GeckoViewModel} and groups all classes that represent
 * {@link org.gecko.viewmodel.AbstractViewModelElement AbstractViewModelElements} corresponding to the elements in the
 * domain model of Gecko. The tree-structure of the model is not preserved here. All elements except
 * {@link org.gecko.viewmodel.ContractViewModel ContractViewModels} are
 * {@link org.gecko.viewmodel.PositionableViewModelElement PositionableViewModelElements}, that is elements with a
 * position and a size in the view. Among these are distinguished {@link org.gecko.viewmodel.BlockViewModelElement
 * BlockViewModelElements}, which are {@link org.gecko.viewmodel.Renamable}, and
 * {@link org.gecko.viewmodel.ConnectionViewModel ConnectionViewModels}. The
 * {@link org.gecko.viewmodel.ViewModelFactory} is responsible for creating instances of each of these elements and the
 * {@link org.gecko.viewmodel.PositionableViewModelElementVisitor} allows access to them. Additionally,
 * the {@link org.gecko.viewmodel.EditorViewModel} models the currently displayed editor view and the
 * {@link org.gecko.viewmodel.SelectionManager} takes account of the selection history.
 */
package org.gecko.viewmodel;