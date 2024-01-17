package org.gecko.application;

import lombok.Getter;
import org.gecko.model.GeckoModel;
import org.gecko.view.GeckoView;
import org.gecko.viewmodel.GeckoViewModel;

@Getter
public class Gecko {
    private final GeckoModel model;
    private final GeckoViewModel viewModel;
    private final GeckoView view;

    public Gecko() {
        // TODO: on import, set system root
        model = new GeckoModel();
        viewModel = new GeckoViewModel(model);
        view = new GeckoView(viewModel.getActionManager(), viewModel);
    }
}
