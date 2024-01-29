package org.gecko.io;

import lombok.Getter;
import lombok.Setter;

/**
 * Wraps two Json Strings: model describes the tree structure of a Gecko Model
 * and viewModelProperties describes ViewModel-specific attributes of PositionableViewModelElements
 * like position coordinates, size coordinates and color values for {@link org.gecko.viewmodel.RegionViewModel
 * RegionViewModels}.
 */
@Getter
@Setter
public class GeckoJsonWrapper {
    private String model;
    private String viewModelProperties;
}
