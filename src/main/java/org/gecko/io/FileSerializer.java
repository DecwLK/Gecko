package org.gecko.io;

import java.io.File;
import java.io.IOException;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public interface FileSerializer {
    void createFile(GeckoModel model, GeckoViewModel viewModel, File file) throws IOException;
}
