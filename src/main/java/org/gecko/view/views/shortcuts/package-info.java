/**
 * Groups classes that manage the use of keyboard shortcuts in Gecko. The defined
 * {@link org.gecko.view.views.shortcuts.Shortcuts} are managed by a
 * {@link org.gecko.view.views.shortcuts.ShortcutHandler} for generally available shortcuts, extended by handlers
 * specific to the active view type:
 * <ul>
 *     <li>
 *         {@link org.gecko.view.views.shortcuts.SystemEditorViewShortcutHandler} handles shortcuts in the system view.
 *     </li>
 *     <li>{@link org.gecko.view.views.shortcuts.AutomatonEditorViewShortcutHandler} handles shortcuts in the automaton
 *     view.</li>
 * </ul>
 *
 */
package org.gecko.view.views.shortcuts;