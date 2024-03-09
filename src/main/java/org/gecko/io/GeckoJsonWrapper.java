package org.gecko.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.System;

/**
 * Wraps two Json Strings: model describes the tree structure of a Gecko Model and viewModelProperties describes
 * ViewModel-specific attributes of PositionableViewModelElements like position coordinates, size coordinates and color
 * values for {@link org.gecko.viewmodel.RegionViewModel RegionViewModels}.
 */
@Getter
@Setter
public class GeckoJsonWrapper {
    private System model;
    private List<StartStateContainer> startStates;
    private List<ViewModelPropertiesContainer> viewModelProperties;

    @JsonCreator
    public GeckoJsonWrapper(
        @JsonProperty("model") System model, @JsonProperty("startStates") List<StartStateContainer> startStates,
        @JsonProperty("viewModelProperties") List<ViewModelPropertiesContainer> viewModelProperties) {
        this.model = model;
        this.startStates = startStates;
        this.viewModelProperties = viewModelProperties;
    }
}
