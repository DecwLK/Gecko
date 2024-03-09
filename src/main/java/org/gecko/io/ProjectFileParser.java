package org.gecko.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
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

        GeckoModel model = new GeckoModel(geckoJsonWrapper.getModel());
        GeckoViewModel viewModel = new GeckoViewModel(model);

        ViewModelElementCreator creator =
            new ViewModelElementCreator(viewModel, geckoJsonWrapper.getViewModelProperties(),
                geckoJsonWrapper.getStartStates());
        creator.traverseModel(model.getRoot());
        updateSystemParents(model.getRoot());

        if (creator.isFoundNonexistentStartState()) {
            throw new IOException("Not all start-states belong to the corresponding automaton's states.");
        }

        if (creator.isFoundNullContainer()) {
            throw new IOException("Not all elements have view model properties.");
        }
        viewModel.getGeckoModel().getModelFactory().setElementId(creator.getHighestId() + 1);
        return viewModel;
    }

    private void updateSystemParents(System system) {
        for (System child : system.getChildren()) {
            child.setParent(system);
            updateSystemParents(child);
        }
    }
}
