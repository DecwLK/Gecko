package org.gecko.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.gecko.model.GeckoModel;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;

/**
 * Provides methods for the conversion of data from a JSON file into Gecko-specific data.
 */
@Getter
public class ProjectFileParser implements FileParser {
    private final ObjectMapper objectMapper;

    public ProjectFileParser() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public GeckoViewModel parse(File file) throws IOException {
        GeckoJsonWrapper geckoJsonWrapper = objectMapper.readValue(file, GeckoJsonWrapper.class);
        if (geckoJsonWrapper == null || geckoJsonWrapper.getModel() == null
            || geckoJsonWrapper.getViewModelProperties() == null) {
            throw new IOException();
        }

        System root = objectMapper.readValue(geckoJsonWrapper.getModel(), System.class);
        TypeReference<ArrayList<ViewModelPropertiesContainer>> typeRef = new TypeReference<>() {
        };
        List<ViewModelPropertiesContainer> newViewModelProperties =
            objectMapper.readValue(geckoJsonWrapper.getViewModelProperties(), typeRef);

        GeckoModel model = new GeckoModel(root);
        GeckoViewModel viewModel = new GeckoViewModel(model);

        ViewModelElementCreator creator = new ViewModelElementCreator(viewModel, newViewModelProperties);
        creator.traverseModel(root);

        if (creator.isFoundNullContainer()) {
            throw new IOException("Not all elements have view model properties.");
        }
        viewModel.getGeckoModel().getModelFactory().setElementId(creator.getHighestId() + 1);
        return viewModel;
    }
}
