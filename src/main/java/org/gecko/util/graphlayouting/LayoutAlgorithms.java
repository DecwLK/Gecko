package org.gecko.util.graphlayouting;

/**
 * LayoutAlgorithms currently supported by Gecko. This enum is simply a listing of used ELK dependencies.
 */
enum LayoutAlgorithms {
    FORCE("force"), LAYERED("layered");

    private final String name;

    LayoutAlgorithms(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "org.eclipse.elk." + name;
    }
}
