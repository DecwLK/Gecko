package org.gecko.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.gecko.model.GeckoModel;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;

public class ProjectFileSerializer implements FileSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void createFile(GeckoModel model, GeckoViewModel viewModel, File file) throws IOException {
        GeckoJsonWrapper geckoJsonWrapper = new GeckoJsonWrapper();
        System root = model.getRoot();

        String rootInJson = this.getRootInJson(root);
        geckoJsonWrapper.setModel(rootInJson);

        ViewModelElementSaveVisitor visitor = new ViewModelElementSaveVisitor(viewModel);
        List<ViewModelPropertiesContainer> viewModelProperties = visitor.getViewModelProperties(root);

        String viewModelPropertiesInJson = this.getViewModelPropertiesInJson(viewModelProperties);
        geckoJsonWrapper.setViewModelProperties(viewModelPropertiesInJson);

        String finalJson = objectMapper.writeValueAsString(geckoJsonWrapper);

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(finalJson);
        fileWriter.close();
    }

    private String getRootInJson(System root) throws JsonProcessingException {
        return objectMapper.writeValueAsString(root);
    }

    private String getViewModelPropertiesInJson(List<ViewModelPropertiesContainer> viewModelProperties) throws JsonProcessingException {
        return objectMapper.writeValueAsString(viewModelProperties);
    }
}
