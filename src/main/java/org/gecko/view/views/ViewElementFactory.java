package org.gecko.view.views;

import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ViewElementFactory {

    public ViewElementFactory() {

    }

    public StateViewElement createStateViewElementFrom(StateViewModel stateViewModel) {
        return new StateViewElement();
    }

    public RegionViewElement createRegionViewElementFrom(RegionViewModel regionViewModel) {
        return new RegionViewElement();
    }

    public VariableBlockViewElement createVariableBlockViewElementFrom(PortViewModel portViewModel) {
        return new VariableBlockViewElement();
    }

    public EdgeViewElement createEdgeViewElementFrom(EdgeViewModel edgeViewModel) {
        return new EdgeViewElement();
    }

    public SystemConnectionViewElement createSystemConnectionViewElementFrom(SystemViewModel systemViewModel) {
        return new SystemConnectionViewElement();
    }

    public SystemViewElement createSystemViewElementFrom(SystemViewModel systemViewModel) {
        return new SystemViewElement();
    }
}
