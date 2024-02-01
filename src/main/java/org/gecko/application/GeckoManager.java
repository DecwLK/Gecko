package org.gecko.application;

import lombok.Getter;
import lombok.Setter;

public class GeckoManager {
    @Getter
    @Setter
    private Gecko gecko;

    public GeckoManager() {
        this.gecko = new Gecko();
    }
}
