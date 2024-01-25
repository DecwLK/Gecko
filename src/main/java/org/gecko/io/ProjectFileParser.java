package org.gecko.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gecko.model.GeckoModel;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class ProjectFileParser implements FileParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<PositionableViewModelElement<?>> generatedViewModelElements;

    public ProjectFileParser() {
        generatedViewModelElements = new ArrayList<>();
    }

    @Override
    public Pair<GeckoModel, GeckoViewModel> parse(File file) throws IOException {
        GeckoJsonWrapper geckoJsonWrapper = objectMapper.readValue(file, GeckoJsonWrapper.class);

        System root = objectMapper.readValue(geckoJsonWrapper.getModel(), System.class);
        TypeReference<ArrayList<ViewModelPropertiesContainer>> typeRef = new TypeReference<>() {
        };
        List<ViewModelPropertiesContainer> newViewModelProperties =
            objectMapper.readValue(geckoJsonWrapper.getViewModelProperties(), typeRef);

        GeckoModel model = new GeckoModel(root);
        GeckoViewModel viewModel = new GeckoViewModel(model);

        ViewModelElementCreatorVisitor visitor =
            new ViewModelElementCreatorVisitor(viewModel.getViewModelFactory(), newViewModelProperties,
                generatedViewModelElements);
        visitor.visitModel(root);
        generatedViewModelElements = visitor.getGeneratedViewModelElements();

        return new Pair<>(model, viewModel);
    }

    public List<PositionableViewModelElement<?>> getGeneratedViewModelElements() {
        return generatedViewModelElements;
    }
}
