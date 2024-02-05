package org.gecko.io;

import lombok.Getter;
import lombok.Setter;

/**
 * Encapsulates {@link org.gecko.viewmodel.GeckoViewModel GeckoViewModel}-specific data for a
 * {@link org.gecko.viewmodel.PositionableViewModelElement PositionableViewModelElement}, useful for the restoration of
 * the {@link org.gecko.view.GeckoView} after parsing an external file.
 */
@Getter
@Setter
public class ViewModelPropertiesContainer {
    private int elementId;
    private int id;
    private double positionX;
    private double positionY;
    private double sizeX;
    private double sizeY;
    private double red;
    private double green;
    private double blue;
}
