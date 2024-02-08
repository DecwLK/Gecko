package org.gecko.application;

import lombok.Getter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.GeckoModel;
import org.gecko.view.GeckoView;
import org.gecko.viewmodel.GeckoViewModel;

@Getter
public class Gecko {
    private final GeckoModel model;
    private final GeckoViewModel viewModel;
    private final GeckoView view;

    public Gecko() throws ModelException {
        model = new GeckoModel();
        viewModel = new GeckoViewModel(model);
        view = new GeckoView(viewModel);
    }

    Gecko(GeckoViewModel geckoViewModel) {
        model = geckoViewModel.getGeckoModel();
        viewModel = geckoViewModel;
        view = new GeckoView(viewModel);
    }
}
