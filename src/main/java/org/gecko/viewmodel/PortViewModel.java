package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

/**
 * Represents an abstraction of a {@link Variable} model element. A {@link PortViewModel} is described by a type and a
 * {@link Visibility}. Contains methods for managing the afferent data and updating the target-{@link Variable}.
 */
@Setter
@Getter
public class PortViewModel extends BlockViewModelElement<Variable> {
    private final Property<Visibility> visibilityProperty;
    private final StringProperty typeProperty;
    private final StringProperty valueProperty;

    private final Property<Point2D> systemPortPositionProperty;
    private final Property<Point2D> systemPortSizeProperty;

    private static final Point2D DEFAULT_PORT_SIZE = new Point2D(100, 50);
    private static final Point2D DEFAULT_SYSTEM_PORT_SIZE = new Point2D(0, 0);

    public PortViewModel(int id, @NonNull Variable target) {
        super(id, target);
        this.visibilityProperty = new SimpleObjectProperty<>(target.getVisibility());
        this.typeProperty = new SimpleStringProperty(target.getType());
        this.valueProperty = new SimpleStringProperty(target.getValue());
        this.sizeProperty.setValue(DEFAULT_PORT_SIZE);
        this.systemPortPositionProperty = new SimpleObjectProperty<>(PositionableViewModelElement.DEFAULT_POSITION);
        this.systemPortSizeProperty = new SimpleObjectProperty<>(DEFAULT_SYSTEM_PORT_SIZE);
    }

    public void setSystemPortPosition(@NonNull Point2D position) {
        systemPortPositionProperty.setValue(position);
    }

    public void setSystemPortSize(@NonNull Point2D size) {
        systemPortSizeProperty.setValue(size);
    }

    public Visibility getVisibility() {
        return visibilityProperty.getValue();
    }

    public void setVisibility(@NonNull Visibility visibility) {
        visibilityProperty.setValue(visibility);
    }

    public String getType() {
        return typeProperty.getValue();
    }

    public void setType(@NonNull String type) {
        typeProperty.setValue(type);
    }

    public String getValue() {
        return valueProperty.getValue();
    }

    public void setValue(String value) {
        valueProperty.setValue(value);
    }

    @Override
    public void updateTarget() throws ModelException {
        super.updateTarget();
        target.setVisibility(getVisibility());
        target.setType(getType());
        target.setValue(getValue());
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }

    public static Color getBackgroundColor(Visibility visibility) {
        return switch (visibility) {
            case INPUT -> Color.LIGHTGREEN;
            case OUTPUT -> Color.LIGHTGOLDENRODYELLOW;
            case STATE -> Color.LIGHTSEAGREEN;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortViewModel port)) {
            return false;
        }
        return id == port.id;
    }
}
