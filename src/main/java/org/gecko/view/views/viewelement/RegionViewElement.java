package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

/**
 * Represents a type of {@link BlockViewElement} implementing the {@link ViewElement} interface, which encapsulates an
 * {@link RegionViewModel}.
 */
@Getter
public class RegionViewElement extends BlockViewElement implements ViewElement<RegionViewModel> {

    private static final int Z_PRIORITY = 10;

    @Getter(AccessLevel.NONE)
    private final RegionViewModel regionViewModel;
    private final StringProperty nameProperty;
    private final Property<Color> colorProperty;
    private final StringProperty invariantProperty;
    private final List<StateViewModel> states;

    private static final String STYLE = "region-view-element";
    private static final String INNER_STYLE = "region-inner-view-element";

    public RegionViewElement(RegionViewModel regionViewModel) {
        super(regionViewModel);
        this.colorProperty = new SimpleObjectProperty<>();
        this.invariantProperty = new SimpleStringProperty();
        this.nameProperty = new SimpleStringProperty();
        this.states = new ArrayList<>();
        this.regionViewModel = regionViewModel;
        bindViewModel();
        constructViewElement();
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public boolean setEdgePoint(int index, Point2D point) {
        return regionViewModel.manipulate(
            getEdgePoints().get((index + getEdgePoints().size() / 2) % getEdgePoints().size()).getValue(), point);
    }

    @Override
    public RegionViewModel getTarget() {
        return regionViewModel;
    }

    @Override
    public Point2D getPosition() {
        return regionViewModel.getPosition();
    }

    @Override
    public int getZPriority() {
        return Z_PRIORITY;
    }

    private void bindViewModel() {
        nameProperty.bind(regionViewModel.getNameProperty());
        colorProperty.bind(regionViewModel.getColorProperty());
        invariantProperty.bind(regionViewModel.getInvariantProperty());
        ListChangeListener<StateViewModel> listener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    states.addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    states.removeAll(change.getRemoved());
                }
            }
        };
        regionViewModel.getStatesProperty().addListener(listener);
        prefWidthProperty().bind(
            Bindings.createDoubleBinding(() -> regionViewModel.getSize().getX(), regionViewModel.getSizeProperty()));
        prefHeightProperty().bind(
            Bindings.createDoubleBinding(() -> regionViewModel.getSize().getY(), regionViewModel.getSizeProperty()));
    }

    private void constructViewElement() {
        getStyleClass().add(STYLE);

        Rectangle background = new Rectangle();
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        background.fillProperty()
            .bind(Bindings.createObjectBinding(
                () -> new Color(colorProperty.getValue().getRed(), colorProperty.getValue().getGreen(),
                    colorProperty.getValue().getBlue(), 0.5), regionViewModel.getColorProperty()));
        background.setArcHeight(BACKGROUND_ROUNDING);
        background.setArcWidth(BACKGROUND_ROUNDING);
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add(INNER_STYLE);

        Label nameDesc = new Label(ResourceHandler.getString("Labels", "name") + ": ");
        Label name = new Label();
        name.textProperty().bind(nameProperty);
        Label preConditionDesc = new Label(ResourceHandler.getString("Labels", "pre_condition_short") + ": ");
        Label preCondition = new Label();
        preCondition.textProperty().bind(regionViewModel.getContract().getPreConditionProperty());
        Label postConditionDesc = new Label(ResourceHandler.getString("Labels", "post_condition_short") + ": ");
        Label postCondition = new Label();
        postCondition.textProperty().bind(regionViewModel.getContract().getPostConditionProperty());
        Label invariantDesc = new Label(ResourceHandler.getString("Labels", "invariant_short") + ": ");
        Label invariant = new Label();
        invariant.textProperty().bind(invariantProperty);
        gridPane.add(nameDesc, 0, 0);
        gridPane.add(name, 1, 0);
        gridPane.add(preConditionDesc, 0, 1);
        gridPane.add(preCondition, 1, 1);
        gridPane.add(postConditionDesc, 0, 2);
        gridPane.add(postCondition, 1, 2);
        gridPane.add(invariantDesc, 0, 3);
        gridPane.add(invariant, 1, 3);
        getChildren().addAll(background, gridPane);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
