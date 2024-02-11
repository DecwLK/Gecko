/**
 * Groups classes which model tools that can be used in Gecko. They are outlined by a {@link org.gecko.tools.Tool} with
 * access to the {@link org.gecko.actions.ActionManager ActionManager}, which provides a tool with the ability to enable
 * operations. Tools are defined by a {@link org.gecko.tools.ToolType} and fall into the following categories:
 * <ul>
 *     <li>
 *         <b>Creator Tools</b>: These tools run create-{@link org.gecko.actions.Action Actions} in order to create
 *         {@link org.gecko.viewmodel.PositionableViewModelElement PositionableViewModelElements}.
 *     </li>
 *
 *     <li>
 *         <b>Selection Tools</b>: These tools run selection-, moving- and scaling-{@link org.gecko.actions.Action
 *         Actions} in order to arrange one or more displayed elements.
 *     </li>
 *
 *     <li>
 *         <b>View Tools</b>: These tools run view-switch-, pan- and zoom-{@link org.gecko.actions.Action Actions} in
 *         order to navigate the view.
 *     </li>
 * </ul>
 */
package org.gecko.tools;