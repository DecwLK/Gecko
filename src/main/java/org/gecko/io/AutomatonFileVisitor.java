package org.gecko.io;

import gecko.parser.SystemDefBaseVisitor;
import gecko.parser.SystemDefParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Automaton;
import org.gecko.model.Condition;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.GeckoModel;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

/**
 * Used for building a {@link GeckoModel} from a sys file. This class is a visitor for the ANTLR4 generated parser for
 * the sys file format. The entire {@link GeckoModel} can be built by calling {@link #visitModel} and passing it the
 * {@link SystemDefParser.ModelContext} of a sys file.
 */
public class AutomatonFileVisitor extends SystemDefBaseVisitor<Void> {

    @Getter
    private GeckoModel model;
    @Getter
    private final Set<String> warnings;

    private System currentSystem;
    private String nextSystemName;
    private AutomatonFileScout scout;


    private static final String START_STATE_REGEX = "[a-z].*";
    private static final String SELF_REFERENCE_TOKEN = "self";

    public AutomatonFileVisitor() throws ModelException {
        this.model = new GeckoModel();
        this.warnings = new TreeSet<>();
        currentSystem = model.getRoot();
    }

    @Override
    public Void visitModel(SystemDefParser.ModelContext ctx) {
        scout = new AutomatonFileScout(ctx);
        if (hasCyclicChildSystems(ctx)) {
            throw new RuntimeException("Cyclic child systems found");
        }
        String rootName;
        if (scout.getRootChildren().size() == 1) {
            rootName = scout.getRootChildren().iterator().next().ident().getText();
        } else if (scout.getRootChildren().size() > 1) {
            List<String> rootNames = scout.getRootChildren().stream().map(ctx1 -> ctx1.ident().getText()).toList();
            rootName = makeUserChooseSystem(rootNames);
            if (rootName == null) {
                throw new RuntimeException("No root system chosen");
            }
        } else {
            throw new RuntimeException("No root system found");
        }
        scout.getSystem(rootName).accept(this);
        System newRoot = model.getRoot()
            .getChildren()
            .iterator()
            .next(); //Because rootChildren had size 1, root now has exactly 1 child
        newRoot.setParent(null);
        model = new GeckoModel(newRoot);
        if (ctx.globalCode != null) {
            model.setGlobalCode(cleanCode(ctx.globalCode.getText()));
        }
        if (ctx.defines() != null) {
            model.setGlobalDefines(ctx.defines().getText());
        }
        return null;
    }

    @Override
    public Void visitSystem(SystemDefParser.SystemContext ctx) {
        System system = buildSystem(ctx);
        currentSystem = system;
        for (SystemDefParser.IoContext io : ctx.io()) {
            io.accept(this);
        }
        for (AutomatonFileScout.SystemInfo child : scout.getChildSystemInfos(ctx)) {
            if (scout.getSystem(child.type()) == null) {
                throw new RuntimeException("System %s not found".formatted(child.type()));
            }
            SystemDefParser.SystemContext childCtx = scout.getSystem(child.type());
            nextSystemName = child.name();
            childCtx.accept(this);
        }
        for (SystemDefParser.ConnectionContext connection : ctx.connection()) {
            connection.accept(this);
        }
        if (!ctx.use_contracts().isEmpty()) {
            if (ctx.use_contracts().size() > 1 || ctx.use_contracts().getFirst().use_contract().size() > 1) {
                throw new RuntimeException("Multiple automata in one system are not supported");
            }
            ctx.use_contracts().getFirst().use_contract().getFirst().accept(this);
        }
        currentSystem = system.getParent();
        return null;
    }

    @Override
    public Void visitUse_contract(SystemDefParser.Use_contractContext ctx) {
        SystemDefParser.AutomataContext automata = scout.getAutomaton(ctx.ident().getText());
        if (automata == null) {
            throw new RuntimeException("Automaton %s not found".formatted(ctx.ident().getText()));
        }
        automata.accept(this);
        for (SystemDefParser.SubstContext subst : ctx.subst()) {
            subst.accept(this);
        }
        return null;
    }

    @Override
    public Void visitSubst(SystemDefParser.SubstContext ctx) {
        String toReplace = ctx.local.getText();
        if (ctx.from.inst != null) {
            throw new RuntimeException("Variables to substitute can only be from the same system");
        }
        String toReplaceWith = ctx.from.port.getText();
        if (currentSystem.getVariables().stream().noneMatch(variable -> variable.getName().equals(toReplaceWith))) {
            throw new RuntimeException(
                "Variable %s not found not found in system %s".formatted(toReplace, currentSystem.getName()));
        }
        applySubstitution(currentSystem, toReplace, toReplaceWith);
        return null;
    }

    @Override
    public Void visitAutomata(SystemDefParser.AutomataContext ctx) {
        if (!ctx.history().isEmpty()) {
            warnings.add("Automaton %s has history, which is ignored".formatted(ctx.ident().getText()));
        }
        if (!ctx.use_contracts().isEmpty()) {
            warnings.add("Automaton %s has use_contracts, which are be ignored".formatted(ctx.ident().getText()));
        }
        if (ctx.transition().isEmpty()) {
            warnings.add("Automaton %s has no transitions".formatted(ctx.ident().getText()));
            return null;
        }
        for (SystemDefParser.TransitionContext transition : ctx.transition()) {
            transition.accept(this);
        }
        List<State> startStateCandidates = currentSystem.getAutomaton()
            .getStates()
            .stream()
            .filter(state -> state.getName().matches(START_STATE_REGEX))
            .toList();
        if (startStateCandidates.size() > 1) {
            throw new RuntimeException("Multiple start states found in automaton %s".formatted(ctx.ident().getText()));
        } else if (startStateCandidates.size() == 1) {
            try {
                currentSystem.getAutomaton().setStartState(startStateCandidates.getFirst());
            } catch (ModelException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            try {
                //this should always work because if we have a transition, we have a state
                currentSystem.getAutomaton().setStartState(currentSystem.getAutomaton().getStates().iterator().next());
            } catch (ModelException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Void visitTransition(SystemDefParser.TransitionContext ctx) {
        if (ctx.vvguard() != null) {
            warnings.add("Transition %s has a vvguard, which is ignored".formatted(ctx.getText()));
        }
        String startName = ctx.from.getText();
        String endName = ctx.to.getText();
        State start = currentSystem.getAutomaton().getStateByName(startName);
        if (start == null) {
            try {
                start = model.getModelFactory().createState(currentSystem.getAutomaton());
                start.setName(startName);
            } catch (ModelException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        State end = currentSystem.getAutomaton().getStateByName(endName);
        if (end == null) {
            try {
                end = model.getModelFactory().createState(currentSystem.getAutomaton());
                end.setName(endName);
            } catch (ModelException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        Contract contract;
        if (ctx.contr != null) {
            contract = buildContract(start, scout.getContract(ctx.contr.getText()));
        } else {
            contract = buildContract(start, ctx.pre.getText(), ctx.post.getText());
        }
        Edge edge;
        try {
            edge = model.getModelFactory().createEdge(currentSystem.getAutomaton(), start, end);
        } catch (ModelException e) {
            throw new RuntimeException(e.getMessage());
        }
        edge.setContract(contract);
        return null;
    }

    @Override
    public Void visitIo(SystemDefParser.IoContext ctx) {
        Visibility visibility = switch (ctx.type.getType()) {
            case SystemDefParser.INPUT -> Visibility.INPUT;
            case SystemDefParser.OUTPUT -> Visibility.OUTPUT;
            case SystemDefParser.STATE -> Visibility.STATE;
            default -> throw new IllegalStateException("Unexpected variable visibility: " + ctx.type.getType());
        };
        for (SystemDefParser.VariableContext variable : ctx.variable()) {
            if (!Variable.getBuiltinTypes().contains(variable.t.getText())) {
                if (visibility == Visibility.STATE) {
                    if (scout.getSystem(variable.t.getText()) != null) {
                        return null;
                    } else {
                        throw new RuntimeException("State type must be a system or builtin type");
                    }
                } else {
                    throw new RuntimeException("Input and Output type must be a builtin type");
                }
            }
            for (SystemDefParser.IdentContext ident : variable.n) {
                if (currentSystem.getVariableByName(ident.Ident().getText()) != null
                    || scout.getSystem(ident.getText()) != null) {
                    continue;
                }
                Variable var;
                try {
                    var = model.getModelFactory().createVariable(currentSystem);
                    var.setName(ident.Ident().getText());
                    var.setType(variable.t.getText());
                    var.setVisibility(visibility);
                    if (variable.init != null) {
                        var.setValue(variable.init.getText());
                    }
                } catch (ModelException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public Void visitConnection(SystemDefParser.ConnectionContext ctx) {
        if (ctx.from.inst == null || ctx.to.stream().anyMatch(ident -> ident.inst == null)) {
            throw new RuntimeException("Invalid System in connection");
        }
        System startSystem = parseSystemReference(ctx.from.inst.getText());
        Variable start = startSystem.getVariableByName(ctx.from.port.getText());
        if (start == null) {
            throw new RuntimeException("Could not find variable %s".formatted(ctx.from.port.getText()));
        }
        Set<Variable> end = new HashSet<>();
        for (SystemDefParser.IoportContext ident : ctx.to) {
            System endSystem = parseSystemReference(ident.inst.getText());
            Variable endVar = endSystem.getVariableByName(ident.port.getText());
            if (endVar == null) {
                throw new RuntimeException("Could not find variable %s".formatted(ident.port.getText()));
            }
            end.add(endVar);
        }
        try {
            for (Variable variable : end) {
                model.getModelFactory().createSystemConnection(currentSystem, start, variable);
            }
        } catch (ModelException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    private System buildSystem(SystemDefParser.SystemContext ctx) {
        System system;
        try {
            system = model.getModelFactory().createSystem(currentSystem);
        } catch (ModelException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (nextSystemName != null) {
            try {
                system.setName(nextSystemName);
            } catch (ModelException e) {
                throw new RuntimeException(e.getMessage());
            }
            nextSystemName = null;
        } else {
            try {
                system.setName(ctx.ident().Ident().getText());
            } catch (ModelException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (ctx.reaction() != null) {
            try {
                system.setCode(cleanCode(ctx.reaction().getText()));
            } catch (ModelException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return system;
    }

    private Contract buildContract(State state, SystemDefParser.PrepostContext contract) {
        Contract c = buildContract(state, contract.pre.getText(), contract.post.getText());
        try {
            c.setName(contract.name.getText());
        } catch (ModelException e) {
            throw new RuntimeException(e.getMessage());
        }
        return c;
    }

    private Contract buildContract(State state, String pre, String post) {
        Contract newContract;
        Condition preCondition;
        Condition postCondition;
        try {
            newContract = model.getModelFactory().createContract(state);
            preCondition = model.getModelFactory().createCondition(pre);
            postCondition = model.getModelFactory().createCondition(post);
            newContract.setPreCondition(preCondition);
            newContract.setPostCondition(postCondition);
        } catch (ModelException e) {
            throw new RuntimeException(e.getMessage());
        }
        return newContract;
    }

    private void applySubstitution(System currentSystem, String toReplace, String toReplaceWith) {
        Automaton automaton = currentSystem.getAutomaton();
        for (State state : automaton.getStates()) {
            for (Contract contract : state.getContracts()) {
                applySubstitution(contract.getPreCondition(), toReplace, toReplaceWith);
                applySubstitution(contract.getPostCondition(), toReplace, toReplaceWith);
            }
        }
    }

    private void applySubstitution(Condition condition, String toReplace, String toReplaceWith) {
        String con = condition.getCondition();
        //replace normal occurrences (var -> newVar)
        con = con.replaceAll("\\b" + toReplace + "\\b", toReplaceWith);
        //replace history occurrences (h_var_\d -> h_newVar_\d)
        con = con.replaceAll("\\bh_" + toReplace + "_(\\d+)\\b", "h_" + toReplaceWith + "_$1");
        try {
            condition.setCondition(con);
        } catch (ModelException e) {
            throw new RuntimeException("Failed to apply substitution");
        }
    }

    private String cleanCode(String code) {
        //length("{=") = 2
        return code.substring(2, code.length() - 2);
    }

    private System parseSystemReference(String name) {
        System system;
        if (name.equals(SELF_REFERENCE_TOKEN)) {
            system = currentSystem;
        } else {
            system = currentSystem.getChildByName(name);
            if (system == null) {
                throw new RuntimeException("Could not find system %s".formatted(name));
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

    private String makeUserChooseSystem(List<String> systemNames) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(systemNames);
        comboBox.setPromptText("Choose a system");

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().add(comboBox);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Found multiple top level systems. Please choose a system as root of the project.");
        alert.getDialogPane().setContent(vBox);

        alert.setOnCloseRequest(event -> {
            ButtonType result = alert.getResult();
            if (result == ButtonType.OK && comboBox.getValue() == null) {
                event.consume(); // Prevent dialog from closing
            }
        });

        alert.showAndWait();

        return comboBox.getValue();
    }
}
