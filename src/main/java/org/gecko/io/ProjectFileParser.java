package org.gecko.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.gecko.model.GeckoModel;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * Provides methods for the conversion of data from a JSON file into Gecko-specific data.
 */
public class ProjectFileParser implements FileParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<PositionableViewModelElement<?>> generatedViewModelElements;

    public ProjectFileParser() {
        generatedViewModelElements = new ArrayList<>();
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

        ViewModelElementCreatorVisitor visitor =
            new ViewModelElementCreatorVisitor(viewModel, newViewModelProperties);
        visitor.visitModel(root);
        generatedViewModelElements = visitor.getGeneratedViewModelElements();
        return viewModel;
    }

    public List<PositionableViewModelElement<?>> getGeneratedViewModelElements() {
        return generatedViewModelElements;
    }
}
