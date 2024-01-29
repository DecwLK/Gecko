package org.gecko.io;

import java.io.File;
import java.io.IOException;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

/**
 * Provides methods for the conversion of Gecko-specific data to a different format
 * and writing the converted data in a desired file format.
 */
public interface FileSerializer {
    void createFile(GeckoModel model, GeckoViewModel viewModel, File file) throws IOException;
}
