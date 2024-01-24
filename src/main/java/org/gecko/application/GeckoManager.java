package org.gecko.application;

import lombok.Getter;
import lombok.Setter;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public class GeckoManager {
    @Getter
    @Setter
    private Gecko gecko;

    public GeckoManager() {
        this.gecko = new Gecko();
    }

    protected void setGecko(GeckoModel geckoModel, GeckoViewModel geckoViewModel) {
        this.gecko = new Gecko(geckoModel, geckoViewModel);
    }
}
