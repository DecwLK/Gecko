/**
 * Groups classes which model, create and manage operations that can be performed in Gecko. These operations,
 * called {@link org.gecko.actions.Action Actions}, can be either run or undone by the
 * {@link org.gecko.actions.ActionManager}. The {@link org.gecko.actions.ActionFactory} is responsible for creating
 * instances of each of these actions. Actions can also be grouped in an {@link org.gecko.actions.ActionGroup}, which
 * represents an action that runs or undoes multiple actions iteratively. The actions fall into the following
 * categories:
 * <ul>
 *     <li>
 *         <b>Creating {@link org.gecko.viewmodel.AbstractViewModelElement AbstractViewModelElements}</b>:
 *         These actions use the {@link org.gecko.viewmodel.ViewModelFactory ViewModelFactory} to create
 *         view model elements and adapt them in order to be properly displayed in the view.
 *         Undoing an element's creation is provided through an afferent delete-action.
 *     </li>
 *
 *     <li>
 *         <b>Deleting {@link org.gecko.viewmodel.AbstractViewModelElement AbstractViewModelElements}</b>:
 *         These actions remove view model elements from the view model, their targets from the model,
 *         as well as their dependencies to other elements. Undoing an element's deletion is provided
 *         through an afferent restore-action.
 *     </li>
 *
 *     <li>
 *         <b>Restoring {@link org.gecko.viewmodel.AbstractViewModelElement AbstractViewModelElements}</b>:
 *         These actions restore previously deleted view model elements, by adding them back to the view model.
 *         Undoing an element's restoration is provided through an afferent delete-action.
 *     </li>
 *
 *     <li>
 *         <b>Changing the properties of {@link org.gecko.viewmodel.AbstractViewModelElement
 *         AbstractViewModelElements}</b>: These actions change the values of different properties of the view model
 *         elements. Undoing a property change is provided through the same change-action given the old value of the
 *         property.
 *     </li>
 *
 *     <li>
 *         <b>Renaming {@link org.gecko.viewmodel.BlockViewModelElement BlockViewModelElements}</b>:
 *         These actions change the name of renamable view model elements. Undoing a name change is provided through
 *         the same rename-action given the old name of the element.
 *     </li>
 *
 *     <li>
 *         <b>Copying, pasting or cutting {@link org.gecko.viewmodel.PositionableViewModelElement
 *         PositionableViewModelElements}</b>: These actions manage the creation of new view model elements with the
 *         same property-values as still existing or just deleted ones. Undoing the paste of a view model element is
 *         provided through an afferent delete-action, while undoing the cut of a view model element is provided by
 *         an afferent restore-action. Undoing the copying of a view model element is not supported.
 *     </li>
 *
 *     <li>
 *         <b>Selecting, deselecting and focusing {@link org.gecko.viewmodel.PositionableViewModelElement
 *         PositionableViewModelElements}</b>: These actions manage the selection and deselection of view model
 *         elements, as well as focusing on a certain view model element, which means selecting it and moving to it
 *         in the view. Undoing these actions is not supported.
 *     </li>
 *
 *     <li>
 *         <b>Moving and scaling {@link org.gecko.viewmodel.PositionableViewModelElement
 *         PositionableViewModelElements}</b>: These actions manage moving the view model elements in the view or
 *         scaling them to a different size. Undoing the move of a view model element is provided by the same
 *         move-action, with the same distance but reversed direction, while undoing the scale is provided by the same
 *         scale-action given the old position and values.
 *     </li>
 *
 *     <li>
 *         <b>Switching views</b>: This action switches between the system view and the automaton view of a current
 *         system. Undoing the view switch is provided by the same view-switch-action given the old view's values.
 *     </li>
 *
 *     <li>
 *         <b>Zooming in or out of the view</b>: These actions zoom in or out of the view by a given factor from a fixed
 *         or given pivot. Undoing the zoom is not supported.
 *     </li>
 * </ul>
 */
package org.gecko.actions;