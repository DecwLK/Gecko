package org.gecko.util;

import org.gecko.exceptions.ModelException;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public class TestHelper {
    public static GeckoViewModel createGeckoViewModel() throws ModelException {
        GeckoModel geckoModel = new GeckoModel();
        return new GeckoViewModel(geckoModel);
    }
}
