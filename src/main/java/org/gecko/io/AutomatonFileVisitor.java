package org.gecko.io;

import gecko.parser.SystemDefBaseVisitor;
import gecko.parser.SystemDefParser;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
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
    private AutomatonFileScout scout;

    public AutomatonFileVisitor() {
        this.model = new GeckoModel();
        currentSystem = model.getRoot();
    }

    @Override
    public String visitModel(SystemDefParser.ModelContext ctx) {
        scout = new AutomatonFileScout(ctx);
        if (scout.getRootChildren().size() == 1) {
            //We can use the first root system as the root of the model
            String result = scout.getSystem(scout.getRootChildren().iterator().next().ident().getText()).accept(this);
            if (result != null) {
                return result;
            }
            System newRoot = model.getRoot()
                .getChildren()
                .iterator()
                .next(); //Because rootChildren has size 1 root now has exactly 1 child
            newRoot.setParent(null);
            model = new GeckoModel(newRoot);
        } else if (scout.getRootChildren().size() > 1) {
            //We need to create a new root system and add the old roots as children
            scout.getRootChildren().forEach(this::visitSystem);
        } else {
            return "No root system found"; //TODO
        }
        return null;
    }

    @Override
    public String visitSystem(SystemDefParser.SystemContext ctx) {
        System system = buildSystem(ctx);
        currentSystem = system;
        for (SystemDefParser.IoContext io : ctx.io()) {
            String result = io.accept(this);
            if (result != null) {
                return result;
            }
        }
        for (String child : scout.getChildSystemNames(ctx)) {
            SystemDefParser.SystemContext childCtx = scout.getSystem(child);
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
        if (ctx.use_contracts() != null) {
            if (ctx.use_contracts().size() > 1 || ctx.use_contracts().getFirst().use_contract().size() > 1) {
                return "Multiple automata not supported"; //TODO
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
            return "Substitution not supported"; //TODO
        }
        SystemDefParser.AutomataContext automata = scout.getAutomaton(ctx.ident().getText());
        if (automata == null) {
            return "Automaton not found"; //TODO
        }
        return automata.accept(this);
    }

    @Override
    public String visitAutomata(SystemDefParser.AutomataContext ctx) {
        if (!(ctx.io().isEmpty() && ctx.history().isEmpty() && ctx.prepost().isEmpty() && ctx.use_contracts()
            .isEmpty())) {
            return "Unsupported automata features"; //TODO
        }
        for (SystemDefParser.TransitionContext transition : ctx.transition()) {
            String result = transition.accept(this);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public String visitTransition(SystemDefParser.TransitionContext ctx) {
        if (ctx.vvguard() != null || ctx.contr != null) {
            return "Unsupported transition features"; //TODO
        }
        String startName = ctx.from.getText();
        String endName = ctx.to.getText();
        State start = currentSystem.getAutomaton().getStateByName(startName);
        if (start == null) {
            start = model.getModelFactory().createState(currentSystem.getAutomaton());
            start.setName(startName);
        }
        State end = currentSystem.getAutomaton().getStateByName(endName);
        if (end == null) {
            end = model.getModelFactory().createState(currentSystem.getAutomaton());
            end.setName(endName);
        }
        Contract contract = model.getModelFactory().createContract(start);
        contract.setPreCondition(model.getModelFactory().createCondition(ctx.pre.getText()));
        contract.setPostCondition(model.getModelFactory().createCondition(ctx.post.getText()));
        Edge edge = model.getModelFactory().createEdge(currentSystem.getAutomaton(), start, end);
        edge.setContract(contract);
        return null;
    }

    @Override
    public String visitIo(SystemDefParser.IoContext ctx) {
        Visibility visibility = switch (ctx.type.getType()) {
            case SystemDefParser.INPUT -> Visibility.INPUT;
            case SystemDefParser.OUTPUT -> Visibility.OUTPUT;
            default -> null;
        };
        if (visibility == null) {
            return null;
        }
        for (SystemDefParser.VariableContext variable : ctx.variable()) {
            if (!Variable.getBuiltinTypes().contains(variable.t.getText())) {
                if (scout.getSystem(variable.t.getText()) == null) {
                    return "Invalid type"; //TODO
                }
                continue;
            }
            for (SystemDefParser.IdentContext ident : variable.n) {
                Variable var = model.getModelFactory().createVariable(currentSystem);
                var.setName(ident.Ident().getText());
                var.setType(variable.t.getText());
                var.setVisibility(visibility);
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
            return "Invalid connection"; //TODO
        }
        System startSystem = currentSystem.getChildByName(ctx.from.inst.getText());
        if (startSystem == null) {
            return "Invalid connection"; //TODO
        }
        Variable start = startSystem.getVariableByName(ctx.from.port.getText());
        if (start == null) {
            return "Invalid connection"; //TODO
        }
        Set<Variable> end = new HashSet<>();
        for (SystemDefParser.IoportContext ident : ctx.to) {
            System endSystem = currentSystem.getChildByName(ident.inst.getText());
            if (endSystem == null) {
                return "Invalid connection"; //TODO
            }
            Variable endVar = endSystem.getVariableByName(ident.port.getText());
            if (endVar == null) {
                return "Invalid connection"; //TODO
            }
            end.add(endVar);
        }
        for (Variable variable : end) {
            model.getModelFactory().createSystemConnection(currentSystem, start, variable); //TODO this should throw
        }
        return null;
    }

    private System buildSystem(SystemDefParser.SystemContext ctx) {
        System system = model.getModelFactory().createSystem(currentSystem);
        system.setName(ctx.ident().Ident().getText());
        if (ctx.reaction() != null) {
            system.setCode(cleanCode(ctx.reaction().getText()));
        }
        return system;
    }

    private String cleanCode(String code) {
        return code.substring(2, code.length() - 2);
    }
}
