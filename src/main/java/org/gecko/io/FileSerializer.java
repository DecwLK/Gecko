package org.gecko.io;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.gecko.application.Gecko;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public interface FileSerializer {
    // void createFile(Gecko gecko) throws IOException;
    void createFile(GeckoModel model, GeckoViewModel viewModel, String file) throws IOException;
}
