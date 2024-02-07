package org.gecko.io;

import java.io.File;
import java.io.IOException;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.viewmodel.GeckoViewModel;

/**
 * Provides methods for the conversion of data from an external file into Gecko-specific data.
 */
public interface FileParser {
    GeckoViewModel parse(File file) throws IOException, MissingViewModelElementException;
}
