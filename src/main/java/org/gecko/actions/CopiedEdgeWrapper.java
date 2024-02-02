package org.gecko.actions;

import lombok.Data;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

@Data
public class CopiedEdgeWrapper {
    private EdgeViewModel edge;
    private StateViewModel source;
    private StateViewModel destination;

    public CopiedEdgeWrapper(EdgeViewModel edge, StateViewModel source, StateViewModel destination) {
        this.edge = edge;
        this.source = source;
        this.destination = destination;
    }
}
