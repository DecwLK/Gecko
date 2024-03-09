package org.gecko.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;

/**
 * Provides methods for the conversion of Gecko-specific data to the JSON format and writing the converted data in a
 * JSON file.
 */
public class ProjectFileSerializer implements FileSerializer {
    private final ObjectMapper objectMapper;
    private final GeckoViewModel viewModel;

    public ProjectFileSerializer(GeckoViewModel viewModel) {
        this.objectMapper = new ObjectMapper();
        this.viewModel = viewModel;
    }

    @Override
    public void writeToFile(File file) throws IOException {
        System root = viewModel.getGeckoModel().getRoot();
        ViewModelElementSaver saver = new ViewModelElementSaver(viewModel);
        List<StartStateContainer> startStates = saver.getStartStates();
        List<ViewModelPropertiesContainer> viewModelProperties = saver.getViewModelProperties(root);
        GeckoJsonWrapper geckoJsonWrapper = new GeckoJsonWrapper(root, startStates, viewModelProperties);

        String finalJson = objectMapper.writeValueAsString(geckoJsonWrapper);

        Writer fileWriter = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
        fileWriter.write(finalJson);
        fileWriter.close();
    }
}
