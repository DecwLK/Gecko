package org.gecko.util.graphlayouting;

import static org.eclipse.elk.graph.util.ElkGraphUtil.createGraph;
import static org.eclipse.elk.graph.util.ElkGraphUtil.createNode;
import static org.eclipse.elk.graph.util.ElkGraphUtil.createSimpleEdge;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.elk.graph.ElkNode;
import org.gecko.model.System;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ELKGraphCreator {

    private final GeckoViewModel viewModel;

    public ELKGraphCreator(GeckoViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ElkNode createSystemElkGraph(SystemViewModel system) {
        ElkNode root = createGraph();
        List<BlockViewModelElement<?>> children = new ArrayList<>(getChildSystemViewModels(system));
        children.addAll(system.getPorts());
        for (BlockViewModelElement<?> child : children) {
            createElkNode(root, child);
        }
        for (SystemConnectionViewModel connection : getConnectionViewModels(system)) {
            ElkNode start = findPortOrSystemNode(root, system, connection.getSource());
            ElkNode end = findPortOrSystemNode(root, system, connection.getDestination());
            createSimpleEdge(start, end);
        }
        return root;
    }

    public ElkNode createAutomatonElkGraph(SystemViewModel system) {
        ElkNode root = createGraph();
        List<StateViewModel> children = getStates(system);
        for (StateViewModel child : children) {
            createElkNode(root, child);
        }
        for (EdgeViewModel edge : getAutomatonEdges(system)) {
            createSimpleEdge(findNode(root, edge.getSource()), findNode(root, edge.getDestination()));
        }
        return root;
    }

    private void createElkNode(ElkNode parent, BlockViewModelElement<?> block) {
        ElkNode node = createNode(parent);
        node.setX(block.getPosition().getX());
        node.setY(block.getPosition().getY());
        node.setWidth(block.getSize().getX());
        node.setHeight(block.getSize().getY());
        node.setIdentifier(Integer.toString(block.getId()));
    }

    private List<SystemViewModel> getChildSystemViewModels(SystemViewModel systemViewModel) {
        return systemViewModel.getTarget()
            .getChildren()
            .stream()
            .map(s -> (SystemViewModel) viewModel.getViewModelElement(s))
            .toList();
    }

    private List<EdgeViewModel> getAutomatonEdges(SystemViewModel systemViewModel) {
        return systemViewModel.getTarget()
            .getAutomaton()
            .getEdges()
            .stream()
            .map(e -> (EdgeViewModel) viewModel.getViewModelElement(e))
            .toList();
    }

    private List<SystemConnectionViewModel> getConnectionViewModels(SystemViewModel systemViewModel) {
        return systemViewModel.getTarget()
            .getConnections()
            .stream()
            .map(c -> (SystemConnectionViewModel) viewModel.getViewModelElement(c))
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

    private ElkNode findNode(ElkNode graph, BlockViewModelElement<?> element) {
        return graph.getChildren()
            .stream()
            .filter(e -> e.getIdentifier().equals(Integer.toString(element.getId())))
            .findFirst()
            .orElse(null);
    }

    private ElkNode findPortOrSystemNode(ElkNode graph, SystemViewModel parentSystem, PortViewModel element) {
        if (parentSystem.getPorts().contains(element)) {
            return findNode(graph, element);
        } else {
            System sys = parentSystem.getTarget().getChildSystemWithVariable(element.getTarget());
            return findNode(graph, (SystemViewModel) viewModel.getViewModelElement(sys));
        }
    }
}
