package org.gecko.io;

import gecko.parser.SystemDefBaseVisitor;
import gecko.parser.SystemDefParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.gecko.exceptions.GeckoException;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Condition;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.GeckoModel;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

@Getter
public class AutomatonFileVisitor extends SystemDefBaseVisitor<String> {

    private GeckoModel model;
    private System currentSystem;
    private String nextSystemName;
    private AutomatonFileScout scout;

    private static final String START_STATE_REGEX = "[a-z].*";
    private static final String SELF_REFERENCE_TOKEN = "self";

    public AutomatonFileVisitor() throws ModelException {
        this.model = new GeckoModel();
        currentSystem = model.getRoot();
    }

    @Override
    public String visitModel(SystemDefParser.ModelContext ctx) {
        scout = new AutomatonFileScout(ctx);
        if (hasCyclicChildSystems(ctx)) {
            return "Cyclic child systems found";
        }
        if (scout.getRootChildren().size() == 1) {
            //We can use the first root system as the root of the model
            String result = scout.getSystem(scout.getRootChildren().iterator().next().ident().getText()).accept(this);
            if (result != null) {
                return result;
            }
            System newRoot = model.getRoot()
                .getChildren()
                .iterator()
                .next(); //Because rootChildren had size 1, root now has exactly 1 child
            newRoot.setParent(null);
            model = new GeckoModel(newRoot);
        } else if (scout.getRootChildren().size() > 1) {
            //Because we can't have multiple roots, we need to create a new root system
            for (SystemDefParser.SystemContext rootChild : scout.getRootChildren()) {
                String result = rootChild.accept(this);
                if (result != null) {
                    return result;
                }
            }
        } else {
            return "No root system found";
        }
        return null;
    }

    @Override
    public String visitSystem(SystemDefParser.SystemContext ctx) {
        System system;
        try {
            system = buildSystem(ctx);
        } catch (GeckoException e) {
            return e.getMessage();
        }
        currentSystem = system;
        for (SystemDefParser.IoContext io : ctx.io()) {
            String result = io.accept(this);
            if (result != null) {
                return result;
            }
        }
        for (AutomatonFileScout.SystemInfo child : scout.getChildSystemInfos(ctx)) {
            if (scout.getSystem(child.type()) == null) {
                return "System %s not found".formatted(child);
            }
            SystemDefParser.SystemContext childCtx = scout.getSystem(child.type());
            nextSystemName = child.name();
            String result = childCtx.accept(this);
            if (result != null) {
                return result;
            }
        }
        for (SystemDefParser.ConnectionContext connection : ctx.connection()) {
            String result = connection.accept(this);
            if (result != null) {
                return result;
            }
        }
        if (!ctx.use_contracts().isEmpty()) {
            if (ctx.use_contracts().size() > 1 || ctx.use_contracts().getFirst().use_contract().size() > 1) {
                return "Multiple automata in one system not supported";
            }
            String result = ctx.use_contracts().getFirst().use_contract().getFirst().accept(this);
            if (result != null) {
                return result;
            }
        }
        currentSystem = system.getParent();
        return null;
    }

    @Override
    public String visitUse_contract(SystemDefParser.Use_contractContext ctx) {
        if (!ctx.subst().isEmpty()) {
            return "Substitution not supported";
        }
        SystemDefParser.AutomataContext automata = scout.getAutomaton(ctx.ident().getText());
        if (automata == null) {
            return "Automaton %s not found".formatted(ctx.ident().getText());
        }
        return automata.accept(this);
    }

    @Override
    public String visitAutomata(SystemDefParser.AutomataContext ctx) {
        if (!ctx.io().isEmpty()) {
            return "Automata io not supported";
        }
        if (!ctx.history().isEmpty()) {
            return "Automata history not supported";
        }
        if (!ctx.prepost().isEmpty()) {
            return "Automata prepost not supported";
        }
        if (!ctx.use_contracts().isEmpty()) {
            return "Automata use_contracts not supported";
        }
        for (SystemDefParser.TransitionContext transition : ctx.transition()) {
            String result = transition.accept(this);
            if (result != null) {
                return result;
            }
        }
        List<State> startStateCandidates = currentSystem.getAutomaton()
            .getStates()
            .stream()
            .filter(state -> state.getName().matches(START_STATE_REGEX))
            .toList();
        if (startStateCandidates.size() > 1) {
            return "Found multiple start states in automaton %s".formatted(ctx.ident().getText());
        } else if (startStateCandidates.size() == 1) {
            try {
                currentSystem.getAutomaton().setStartState(startStateCandidates.get(0));
            } catch (ModelException e) {
                return e.getMessage();
            }
        }
        return null;
    }

    @Override
    public String visitTransition(SystemDefParser.TransitionContext ctx) {
        if (ctx.vvguard() != null) {
            return "Vvguards are not supported";
        }
        if (ctx.contr != null) {
            return "Nested automata are not supported";
        }
        String startName = ctx.from.getText();
        String endName = ctx.to.getText();
        State start = currentSystem.getAutomaton().getStateByName(startName);
        if (start == null) {
            try {
                start = model.getModelFactory().createState(currentSystem.getAutomaton());
                start.setName(startName);
            } catch (ModelException e) {
                return e.getMessage();
            }
        }
        State end = currentSystem.getAutomaton().getStateByName(endName);
        if (end == null) {
            try {
                end = model.getModelFactory().createState(currentSystem.getAutomaton());
                end.setName(endName);
            } catch (ModelException e) {
                return e.getMessage();
            }
        }
        Contract contract;
        try {
            contract = model.getModelFactory().createContract(start);
        } catch (ModelException e) {
            return e.getMessage();
        }
        Condition pre;
        Condition post;
        try {
            pre = new Condition(ctx.pre.getText());
            post = new Condition(ctx.post.getText());
        } catch (ModelException e) {
            return e.getMessage();
        }
        Edge edge;
        try {
            edge = model.getModelFactory().createEdge(currentSystem.getAutomaton(), start, end);
            edge.setContract(contract);
        } catch (ModelException e) {
            return e.getMessage();
        }
        return null;
    }

    @Override
    public String visitIo(SystemDefParser.IoContext ctx) {
        Visibility visibility = switch (ctx.type.getType()) {
            case SystemDefParser.INPUT -> Visibility.INPUT;
            case SystemDefParser.OUTPUT -> Visibility.OUTPUT;
            case SystemDefParser.STATE -> Visibility.STATE;
            default -> null;
        };
        if (visibility == Visibility.STATE) {
            return null;
        }
        for (SystemDefParser.VariableContext variable : ctx.variable()) {
            if (!Variable.getBuiltinTypes().contains(variable.t.getText())) {
                return "Input and Output types must be built-in types";
            }
            for (SystemDefParser.IdentContext ident : variable.n) {
                if (currentSystem.getVariableByName(ident.Ident().getText()) != null) {
                    continue;
                }
                Variable var;
                try {
                    var = model.getModelFactory().createVariable(currentSystem);
                    var.setName(ident.Ident().getText());
                    var.setType(variable.t.getText());
                    var.setVisibility(visibility);
                } catch (ModelException e) {
                    return e.getMessage();
                }
                if (variable.init != null) {
                    var.setValue(variable.init.getText());
                }
            }
        }
        return null;
    }

    @Override
    public String visitConnection(SystemDefParser.ConnectionContext ctx) {
        if (ctx.from.inst == null || ctx.to.stream().anyMatch(ident -> ident.inst == null)) {
            return "Invalid system in system connection";
        }
        System startSystem;
        try {
            startSystem = parseSystemReference(ctx.from.inst.getText());
        } catch (Exception e) {
            return e.getMessage();
        }
        Variable start = startSystem.getVariableByName(ctx.from.port.getText());
        if (start == null) {
            return "Could not find variable %s".formatted(ctx.from.port.getText());
        }
        Set<Variable> end = new HashSet<>();
        for (SystemDefParser.IoportContext ident : ctx.to) {
            System endSystem;
            try {
                endSystem = parseSystemReference(ident.inst.getText());
            } catch (Exception e) {
                return e.getMessage();
            }
            Variable endVar = endSystem.getVariableByName(ident.port.getText());
            if (endVar == null) {
                return "Could not find variable %s".formatted(ident.port.getText());
            }
            end.add(endVar);
        }
        try {
            for (Variable variable : end) {
                model.getModelFactory().createSystemConnection(currentSystem, start, variable); //TODO this should throw
            }
        } catch (ModelException e) {
            return e.getMessage();
        }
        return null;
    }

    private System buildSystem(SystemDefParser.SystemContext ctx) throws GeckoException {
        System system;
        try {
            system = model.getModelFactory().createSystem(currentSystem);
        } catch (ModelException e) {
            throw new GeckoException(e.getMessage());
        }
        if (nextSystemName != null) {
            try {
                system.setName(nextSystemName);
            } catch (ModelException e) {
                throw new GeckoException(e.getMessage());
            }
            nextSystemName = null;
        } else {
            system.setName(ctx.ident().Ident().getText());
        }
        if (ctx.reaction() != null) {
            system.setCode(cleanCode(ctx.reaction().getText()));
        }
        return system;
    }

    private String cleanCode(String code) {
        return code.substring(2, code.length() - 2);
    }

    private System parseSystemReference(String name) throws GeckoException {
        System system;
        if (name.equals(SELF_REFERENCE_TOKEN)) {
            system = currentSystem;
        } else {
            system = currentSystem.getChildByName(name);
            if (system == null) {
                throw new GeckoException("Could not find system %s".formatted(name));
            }
        }
        return system;
    }

    private boolean hasCyclicChildSystems(SystemDefParser.ModelContext ctx) {
        for (SystemDefParser.SystemContext system : ctx.system()) {
            List<SystemDefParser.SystemContext> parents = new ArrayList<>();
            List<SystemDefParser.SystemContext> currentParents = scout.getParents(system);
            while (currentParents != null && !currentParents.isEmpty()) {
                parents.addAll(currentParents);
                currentParents = currentParents.stream().map(scout::getParents).flatMap(List::stream).toList();
                if (parents.contains(system)) {
                    return true;
                }
            }
        }
        return false;
    }
}
