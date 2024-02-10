package org.gecko.util.graphlayouting;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.util.BasicProgressMonitor;
import org.eclipse.elk.graph.ElkNode;
import org.gecko.model.System;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class Graphlayouter {

    private final GeckoViewModel viewModel;
    private final ELKGraphCreator elkGraphCreator;

    public Graphlayouter(GeckoViewModel viewModel) {
        this.viewModel = viewModel;
        elkGraphCreator = new ELKGraphCreator(viewModel);
    }

    public void layout() {
        SystemViewModel root = (SystemViewModel) viewModel.getViewModelElement(viewModel.getGeckoModel().getRoot());
        layout(root);
    }

    private void layout(SystemViewModel systemViewModel) {
        ElkNode systemGraph = elkGraphCreator.createSystemElkGraph(systemViewModel);
        layoutGraph(systemGraph, LayoutAlgorithms.LAYERED);
        applySystemLayoutToViewModel(systemGraph, systemViewModel);
        ElkNode automatonGraph = elkGraphCreator.createAutomatonElkGraph(systemViewModel);
        layoutGraph(automatonGraph, LayoutAlgorithms.FORCE);
        applyAutomatonLayoutToViewModel(automatonGraph, systemViewModel);
        for (System system : systemViewModel.getTarget().getChildren()) {
            SystemViewModel svm = (SystemViewModel) viewModel.getViewModelElement(system);
            layout(svm);
        }
    }

    private void layoutGraph(ElkNode root, LayoutAlgorithms layoutAlgorithm) {
        if (root.getChildren().isEmpty()) {
            return;
        }
        root.setProperty(CoreOptions.ALGORITHM, layoutAlgorithm.toString());
        double spacing = root.getChildren().getFirst().getWidth() / 3;
        root.setProperty(CoreOptions.SPACING_NODE_NODE, spacing);
        if (layoutAlgorithm == LayoutAlgorithms.LAYERED) {
            root.setProperty(LayeredOptions.SPACING_NODE_NODE_BETWEEN_LAYERS, spacing);
        }
        IGraphLayoutEngine engine = new RecursiveGraphLayoutEngine();
        engine.layout(root, new BasicProgressMonitor());
    }

    private void applySystemLayoutToViewModel(ElkNode root, SystemViewModel viewModel) {
        List<BlockViewModelElement<?>> children = new ArrayList<>(getChildSystemViewModels(viewModel));
        children.addAll(viewModel.getPorts());
        for (BlockViewModelElement<?> child : children) {
            applyLayoutToNode(root, child);
        }
    }

    private void applyAutomatonLayoutToViewModel(ElkNode root, SystemViewModel viewModel) {
        for (StateViewModel child : getStates(viewModel)) {
            applyLayoutToNode(root, child);
        }
    }

    private void applyLayoutToNode(ElkNode root, BlockViewModelElement<?> viewModel) {
        ElkNode node = findNodeById(root, viewModel.getId());
        viewModel.setPosition(new Point2D(node.getX(), node.getY()));
        viewModel.setSize(new Point2D(node.getWidth(), node.getHeight()));
    }

    private ElkNode findNodeById(ElkNode root, int id) {
        return root.getChildren()
            .stream()
            .filter(c -> c.getIdentifier().equals(Integer.toString(id)))
            .findFirst()
            .orElse(null);
    }

    private List<SystemViewModel> getChildSystemViewModels(SystemViewModel systemViewModel) {
        return systemViewModel.getTarget()
            .getChildren()
            .stream()
            .map(s -> (SystemViewModel) viewModel.getViewModelElement(s))
            .toList();
    }

    private List<StateViewModel> getStates(SystemViewModel systemViewModel) {
        return systemViewModel.getTarget()
            .getAutomaton()
            .getStates()
            .stream()
            .map(s -> (StateViewModel) viewModel.getViewModelElement(s))
            .toList();
    }
}
