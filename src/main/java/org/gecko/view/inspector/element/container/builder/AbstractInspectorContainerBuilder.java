package org.gecko.view.inspector.element.container.builder;

import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.container.AbstractInspectorContainerItem;

import java.util.List;

public abstract class AbstractInspectorContainerBuilder {

    public abstract List<InspectorElement<?>> getElements();

    public abstract void addElement(InspectorElement<?> element);

    public abstract AbstractInspectorContainerItem build();
}
