package org.gecko.io;

import gecko.parser.SystemDefBaseVisitor;
import gecko.parser.SystemDefParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.gecko.model.Variable;

/**
 * The AutomatonFileScout is responsible for scanning the parsed automaton file and extracting information about the
 * systems, automata, and contracts. It is used by the {@link AutomatonFileParser} to give access to information about a
 * sys file that would otherwise be hard to obtain while visiting single elements.
 */
final class AutomatonFileScout {

    record SystemInfo(String name, String type) {
    }

    private final Map<String, SystemDefParser.SystemContext> systems;
    private final Map<String, SystemDefParser.AutomataContext> automata;
    private final Map<String, SystemDefParser.PrepostContext> contracts;

    private final Set<SystemInfo> foundChildren;
    private final Set<String> rootChildrenIdents;
    @Getter
    private final Set<SystemDefParser.SystemContext> rootChildren;
    private final Map<SystemDefParser.SystemContext, List<SystemDefParser.SystemContext>> parents;

    private final ScoutVisitor scoutVisitor;

    AutomatonFileScout(SystemDefParser.ModelContext ctx) {
        this.systems = new HashMap<>();
        this.automata = new HashMap<>();
        this.foundChildren = new HashSet<>();
        this.rootChildrenIdents = new HashSet<>();
        this.parents = new HashMap<>();
        this.rootChildren = new HashSet<>();
        this.contracts = new HashMap<>();
        this.scoutVisitor = new ScoutVisitor();
        ctx.accept(scoutVisitor);
    }

    public SystemDefParser.SystemContext getSystem(String name) {
        return systems.get(name);
    }

    public SystemDefParser.AutomataContext getAutomaton(String name) {
        return automata.get(name);
    }

    public SystemDefParser.PrepostContext getContract(String name) {
        return contracts.get(name);
    }

    public List<SystemDefParser.SystemContext> getParents(SystemDefParser.SystemContext ctx) {
        return parents.get(ctx);
    }

    List<SystemInfo> getChildSystemInfos(SystemDefParser.SystemContext ctx) {
        return scoutVisitor.getChildSystems(ctx);
    }

    private class ScoutVisitor extends SystemDefBaseVisitor<Void> {

        @Override
        public Void visitModel(SystemDefParser.ModelContext ctx) {
            ctx.system().forEach(system -> system.accept(this));
            ctx.system().forEach(this::registerParent);
            ctx.contract().forEach(contract -> contract.accept(this));
            SystemDefParser.DefinesContext defines = ctx.defines();
            if (defines != null) {
                defines.variable().forEach(variable -> variable.accept(this));
            }
            foundChildren.stream().map(SystemInfo::type).toList().forEach(rootChildrenIdents::remove);
            rootChildren.addAll(ctx.system()
                .stream()
                .filter(system -> rootChildrenIdents.contains(system.ident().Ident().getText()))
                .toList());
            return null;
        }

        @Override
        public Void visitSystem(SystemDefParser.SystemContext ctx) {
            String sysName = ctx.ident().Ident().getText();
            systems.put(sysName, ctx);
            rootChildrenIdents.add(sysName);
            foundChildren.addAll(getChildSystems(ctx));
            parents.put(ctx, new ArrayList<>());
            return null;
        }

        @Override
        public Void visitContract(SystemDefParser.ContractContext ctx) {
            automata.put(ctx.automata().ident().Ident().getText(), ctx.automata());
            for (SystemDefParser.PrepostContext prepost : ctx.automata().prepost()) {
                contracts.put(prepost.ident().getText(), prepost);
            }
            return null;
        }

        private List<SystemInfo> getChildSystems(SystemDefParser.SystemContext ctx) {
            List<SystemInfo> children = new ArrayList<>();
            ctx.io().stream().filter(io -> io.type.getType() == SystemDefParser.STATE).forEach(io -> {
                children.addAll(io.variable()
                    .stream()
                    .filter(var -> !Variable.getBuiltinTypes().contains(var.t.getText()))
                    .flatMap(var -> var.n.stream().map(n -> new SystemInfo(n.getText(), var.t.getText())))
                    .toList());
            });
            return children;
        }

        private void registerParent(SystemDefParser.SystemContext systemContext) {
            for (SystemInfo child : getChildSystems(systemContext)) {
                SystemDefParser.SystemContext childCtx = systems.get(child.type);
                if (childCtx == null) {
                    continue;
                }
                parents.get(childCtx).add(systemContext);
            }
        }
    }
}
