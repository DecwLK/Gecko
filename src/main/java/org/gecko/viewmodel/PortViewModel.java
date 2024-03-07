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

    private final Property<Point2D> systemPortPositionProperty;
    private final Property<Point2D> systemPortSizeProperty;

    public PortViewModel(int id, @NonNull Variable target) {
        super(id, target);
        this.visibilityProperty = new SimpleObjectProperty<>(target.getVisibility());
        this.typeProperty = new SimpleStringProperty(target.getType());
        this.sizeProperty.setValue(new Point2D(100, 50));
        this.systemPortPositionProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.systemPortSizeProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
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

    @Override
    public void updateTarget() throws ModelException {
        super.updateTarget();
        target.setVisibility(getVisibility());
        target.setType(getType());
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }

    public Color getBackgroundColor() {
        return switch (visibilityProperty.getValue()) {
            case INPUT -> Color.LIGHTGREEN;
            case OUTPUT -> Color.LIGHTGOLDENRODYELLOW;
            case STATE -> Color.LIGHTSEAGREEN;
        };
    }

    public static Color getBackgroundColor(Visibility visibility) {
        return switch (visibility) {
            case INPUT -> Color.LIGHTGREEN;
            case OUTPUT -> Color.LIGHTGOLDENRODYELLOW;
            case STATE -> Color.LIGHTSEAGREEN;
        };
    }
}
