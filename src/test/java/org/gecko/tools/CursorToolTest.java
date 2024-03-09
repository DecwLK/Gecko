package org.gecko.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.gecko.application.GeckoIOManager;
import org.gecko.application.GeckoManager;
import org.gecko.exceptions.ModelException;
import org.gecko.view.GeckoView;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class CursorToolTest {
    private GeckoViewModel geckoViewModel;
    private GeckoView geckoView;
    private SystemViewModel rootSystemViewModel;
    private ViewModelFactory viewModelFactory;
    private StateViewModel source;
    private StateViewModel destination;

    @Start
    private void start(Stage stage) throws ModelException {
        stage.show();

        GeckoManager geckoManager = new GeckoManager(stage);
        GeckoIOManager.getInstance().setGeckoManager(geckoManager);
        GeckoIOManager.getInstance().setStage(stage);

        geckoViewModel = geckoManager.getGecko().getViewModel();
        geckoView = geckoManager.getGecko().getView();

        viewModelFactory = geckoViewModel.getViewModelFactory();
        rootSystemViewModel = geckoViewModel.getCurrentEditor().getCurrentSystem();
        try {
            source = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
            destination = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
            viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
            viewModelFactory.createPortViewModelIn(rootSystemViewModel);
            viewModelFactory.createEdgeViewModelIn(rootSystemViewModel, source, destination);
            viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        } catch (ModelException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            fail();
        }

        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void select(FxRobot robot) {
        assertEquals(geckoView.getCurrentViewProperty().getCurrentViewElements().size(), 4);

        for (ViewElement<?> viewElement : geckoView.getCurrentViewProperty().getCurrentViewElements()) {
            robot.clickOn(viewElement.drawElement(), MouseButton.PRIMARY);
        }

        geckoViewModel.switchEditor(rootSystemViewModel, false);
        for (ViewElement<?> viewElement : geckoView.getCurrentViewProperty().getCurrentViewElements()) {
            robot.clickOn(viewElement.drawElement(), MouseButton.PRIMARY);
        }
    }

    /*
    @Test
    void drag(FxRobot robot) {
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        rootSystemViewModel = geckoViewModel.getCurrentEditor().getCurrentSystem();
        Point2D newPosition = destination.getPosition().add(new Point2D(100, 100));
        destination.setPosition(newPosition);
        ViewElement<?> sourceViewElement = geckoView.getCurrentView()
            .getCurrentViewElements()
            .stream()
            .filter(viewElement -> viewElement.getTarget().equals(source))
            .findFirst()
            .orElse(null);
        ViewElement<?> destinationViewElement = geckoView.getCurrentView()
            .getCurrentViewElements()
            .stream()
            .filter(viewElement -> viewElement.getTarget().equals(destination))
            .findFirst()
            .orElse(null);


        robot.clickOn(sourceViewElement.drawElement(), MouseButton.PRIMARY);
        robot.drag(sourceViewElement.drawElement(), MouseButton.PRIMARY).dropTo(destinationViewElement.drawElement());
    }
     */
}
