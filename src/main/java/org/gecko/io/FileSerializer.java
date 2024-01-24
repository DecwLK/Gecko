package org.gecko.io;

import java.io.IOException;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public interface FileSerializer {
    void createFile(GeckoModel model, GeckoViewModel viewModel, String file) throws IOException;
}
