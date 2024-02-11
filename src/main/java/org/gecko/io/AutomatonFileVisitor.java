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
import org.gecko.exceptions.GeckoException;
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
public class AutomatonFileVisitor extends SystemDefBaseVisitor<String> {

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
    public String visitModel(SystemDefParser.ModelContext ctx) {
        scout = new AutomatonFileScout(ctx);
        if (hasCyclicChildSystems(ctx)) {
            return "Cyclic child systems found";
        }
        String rootName;
        if (scout.getRootChildren().size() == 1) {
            rootName = scout.getRootChildren().iterator().next().ident().getText();
        } else if (scout.getRootChildren().size() > 1) {
            List<String> rootNames = scout.getRootChildren().stream().map(ctx1 -> ctx1.ident().getText()).toList();
            rootName = makeUserChooseSystem(rootNames);
            if (rootName == null) {
                return "Must choose a root system to continue";
            }
        } else {
            return "No root system found";
        }
        String result = scout.getSystem(rootName).accept(this);
        if (result != null) {
            return result;
        }
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
                return "System %s not found".formatted(child.name());
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
        SystemDefParser.AutomataContext automata = scout.getAutomaton(ctx.ident().getText());
        if (automata == null) {
            return "Automaton %s not found".formatted(ctx.ident().getText());
        }
        String result = automata.accept(this);
        if (result != null) {
            return result;
        }
        for (SystemDefParser.SubstContext subst : ctx.subst()) {
            result = subst.accept(this);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public String visitSubst(SystemDefParser.SubstContext ctx) {
        String toReplace = ctx.local.getText();
        if (ctx.from.inst != null) {
            return "Variables to substitute can only be from the same system";
        }
        String toReplaceWith = ctx.from.port.getText();
        if (currentSystem.getVariables().stream().noneMatch(variable -> variable.getName().equals(toReplaceWith))) {
            return "Variable %s not found not found in system %s".formatted(toReplace, currentSystem.getName());
        }
        try {
            applySubstitution(currentSystem, toReplace, toReplaceWith);
        } catch (ModelException e) {
            return e.getMessage();
        }
        return null;
    }

    @Override
    public String visitAutomata(SystemDefParser.AutomataContext ctx) {
        if (!ctx.history().isEmpty()) {
            warnings.add("Automaton %s has history, which is ignored".formatted(ctx.ident().getText()));
        }
        if (!ctx.use_contracts().isEmpty()) {
            warnings.add("Automaton %s has use_contracts, which are be ignored".formatted(ctx.ident().getText()));
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
                currentSystem.getAutomaton().setStartState(startStateCandidates.getFirst());
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
        if (ctx.contr != null) {
            try {
                contract = buildContract(start, scout.getContract(ctx.contr.getText()));
            } catch (GeckoException e) {
                return e.getMessage();
            }
        } else {
            try {
                contract = buildContract(start, ctx.pre.getText(), ctx.post.getText());
            } catch (GeckoException e) {
                return e.getMessage();
            }
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
            default -> throw new IllegalStateException("Unexpected variable visibility: " + ctx.type.getType());
        };
        for (SystemDefParser.VariableContext variable : ctx.variable()) {
            if (!Variable.getBuiltinTypes().contains(variable.t.getText())) {
                if (visibility == Visibility.STATE) {
                    if (scout.getSystem(variable.t.getText()) != null) {
                        return null;
                    } else {
                        return "State type must be a system or builin type";
                    }
                } else {
                    return "Input and Output types must be built-in types";
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
                    return e.getMessage();
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

    private Contract buildContract(State state, SystemDefParser.PrepostContext contract) throws GeckoException {
        return buildContract(state, contract.pre.getText(), contract.post.getText());
    }

    private Contract buildContract(State state, String pre, String post) throws GeckoException {
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
            throw new GeckoException(e.getMessage());
        }
        return newContract;
    }

    private void applySubstitution(System currentSystem, String toReplace, String toReplaceWith) throws ModelException {
        Automaton automaton = currentSystem.getAutomaton();
        for (State state : automaton.getStates()) {
            for (Contract contract : state.getContracts()) {
                applySubstitution(contract.getPreCondition(), toReplace, toReplaceWith);
                applySubstitution(contract.getPostCondition(), toReplace, toReplaceWith);
            }
        }
    }

    private void applySubstitution(Condition condition, String toReplace, String toReplaceWith) throws ModelException {
        String con = condition.getCondition();
        //replace normal occurrences (var -> newVar)
        con = con.replaceAll("\\b" + toReplace + "\\b", toReplaceWith);
        //replace history occurrences (h_var_\d -> h_newVar_\d)
        con = con.replaceAll("\\bh_" + toReplace + "_(\\d+)\\b", "h_" + toReplaceWith + "_$1");
        condition.setCondition(con);
    }

    private String cleanCode(String code) {
        //length("{=") = 2
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
