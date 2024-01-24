package org.gecko.io;

import java.io.File;
import java.io.IOException;
import javafx.util.Pair;
import org.gecko.exceptions.MissingViewModelElement;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public interface FileParser {
    Pair<GeckoModel, GeckoViewModel> parse(File file) throws IOException, MissingViewModelElement;
}
