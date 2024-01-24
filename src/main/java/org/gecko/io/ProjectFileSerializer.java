package org.gecko.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gecko.application.Gecko;
import org.gecko.model.GeckoModel;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;

public class ProjectFileSerializer implements FileSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void createFile(GeckoModel model, GeckoViewModel viewModel, String path) throws IOException {
        GeckoJsonWrapper geckoJsonWrapper = new GeckoJsonWrapper();
        System root = model.getRoot();

        String rootInJson = this.getRootInJson(root);
        geckoJsonWrapper.setModel(rootInJson);

        ViewModelElementSaveVisitor visitor = new ViewModelElementSaveVisitor(viewModel);
        List<ViewModelPropertiesContainer> viewModelProperties = visitor.getViewModelProperties(root);

        String viewModelPropertiesInJson = this.getViewModelPropertiesInJson(viewModelProperties);
        geckoJsonWrapper.setViewModelProperties(viewModelPropertiesInJson);

        String finalJson = objectMapper.writeValueAsString(geckoJsonWrapper);

        FileWriter file = new FileWriter(path); // TODO: Get path, not file in GeckoIOManager.
        file.write(finalJson);
        file.close();
    }

    private String getRootInJson(System root) throws JsonProcessingException {
        return objectMapper.writeValueAsString(root);
    }

    private String getViewModelPropertiesInJson(List<ViewModelPropertiesContainer> viewModelProperties)
        throws JsonProcessingException {
        return objectMapper.writeValueAsString(viewModelProperties);
    }
}
