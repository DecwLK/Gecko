package org.gecko.actions;

import lombok.Data;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

@Data
public class CopiedSystemConnectionWrapper {
    private SystemConnectionViewModel systemConnection;
    private PortViewModel source;
    private PortViewModel destination;
    private SystemViewModel sourceParent;
    private SystemViewModel destinationParent;

    public CopiedSystemConnectionWrapper(SystemConnectionViewModel systemConnectionViewModel,
                                         PortViewModel source, PortViewModel destination) {
        this.systemConnection = systemConnectionViewModel;
        this.source = source;
        this.destination = destination;
    }
}
