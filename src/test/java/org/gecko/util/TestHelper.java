package org.gecko.util;

import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public class TestHelper {
    public static GeckoViewModel createGeckoViewModel() {
        GeckoModel geckoModel = new GeckoModel();
        return new GeckoViewModel(geckoModel);
    }
}
