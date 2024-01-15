package org.gecko.application;

import java.io.File;
import lombok.Getter;
import lombok.Setter;

public class GeckoIOManager {
    @Getter
    @Setter
    private GeckoManager geckoManager;

    private static GeckoIOManager instance;

    public static GeckoIOManager getInstance() {
        if (instance == null) {
            instance = new GeckoIOManager();
        }
        return instance;
    }

    public void loadGeckoProject(File file) {

    }

    public void importAutomatonFile(File file) {

    }

    public void saveGeckoProject(File file) {

    }

    public void exportAutomatonFile(File file) {

    }
}
