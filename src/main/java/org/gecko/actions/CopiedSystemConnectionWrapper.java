package org.gecko.actions;

import lombok.Data;
import org.gecko.viewmodel.GeckoViewModel;
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

    public CopiedSystemConnectionWrapper(
        GeckoViewModel geckoViewModel, SystemViewModel currentSystem,
        SystemConnectionViewModel systemConnectionViewModel, PortViewModel source, PortViewModel destination) {
        this.systemConnection = systemConnectionViewModel;
        this.source = source;
        this.destination = destination;
        this.sourceParent = (SystemViewModel) geckoViewModel.getViewModelElement(
            currentSystem.getTarget().getChildSystemWithVariable(source.getTarget()));
        this.destinationParent = (SystemViewModel) geckoViewModel.getViewModelElement(
            currentSystem.getTarget().getChildSystemWithVariable(destination.getTarget()));
    }
}
