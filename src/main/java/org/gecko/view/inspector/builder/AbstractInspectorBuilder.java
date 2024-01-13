package org.gecko.view.inspector.builder;

import org.gecko.view.inspector.Inspector;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.PositionableViewModelElement;

public abstract class AbstractInspectorBuilder<T extends PositionableViewModelElement<?>> {

    protected EditorView editorView;

    protected AbstractInspectorBuilder(EditorView editorView) {
        this.editorView = editorView;
    }

    public T getViewModel() {
        return null;
    }

    public void addInspectorElement() {

    }

    public Inspector<T> build() {
        return null;
    }
}
