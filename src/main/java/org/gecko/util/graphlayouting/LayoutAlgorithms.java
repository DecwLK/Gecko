package org.gecko.util.graphlayouting;

public enum LayoutAlgorithms {
    FORCE("force"),
    LAYERED("layered");

    private final String name;

    LayoutAlgorithms(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "org.eclipse.elk." + name;
    }
}
