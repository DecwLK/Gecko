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

public final class AutomatonFileScout {

    private final Map<String, SystemDefParser.SystemContext> systems;
    private final Map<String, SystemDefParser.AutomataContext> automata;

    private final Set<String> foundChildren;
    private final Set<String> rootChildrenIdents;
    @Getter
    private final Set<SystemDefParser.SystemContext> rootChildren;

    private final ScoutVisitor scoutVisitor;

    public AutomatonFileScout(SystemDefParser.ModelContext ctx) {
        this.systems = new HashMap<>();
        this.automata = new HashMap<>();
        this.foundChildren = new HashSet<>();
        this.rootChildrenIdents = new HashSet<>();
        this.rootChildren = new HashSet<>();
        this.scoutVisitor = new ScoutVisitor();
        ctx.accept(scoutVisitor);
    }

    public SystemDefParser.SystemContext getSystem(String name) {
        return systems.get(name);
    }

    public SystemDefParser.AutomataContext getAutomaton(String name) {
        return automata.get(name);
    }

    public List<String> getChildSystemNames(SystemDefParser.SystemContext ctx) {
        return scoutVisitor.getChildSystems(ctx);
    }

    private class ScoutVisitor extends SystemDefBaseVisitor<Void> {

        @Override
        public Void visitModel(SystemDefParser.ModelContext ctx) {
            ctx.system().forEach(system -> system.accept(this));
            ctx.contract().forEach(contract -> contract.accept(this));
            SystemDefParser.DefinesContext defines = ctx.defines();
            if (defines != null) {
                defines.variable().forEach(variable -> variable.accept(this));
            }
            rootChildrenIdents.removeAll(foundChildren);
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
            return null;
        }

        @Override
        public Void visitContract(SystemDefParser.ContractContext ctx) {
            automata.put(ctx.automata().ident().Ident().getText(), ctx.automata());
            return null;
        }

        public List<String> getChildSystems(SystemDefParser.SystemContext ctx) {
            List<String> children = new ArrayList<>();
            ctx.io().stream().filter(io -> io.type.getType() == SystemDefParser.STATE).forEach(io -> {
                children.addAll(io.variable()
                    .stream()
                    .map(variable -> variable.t.Ident().getText())
                    .filter(type -> !Variable.getBuiltinTypes().contains(type))
                    .toList());
            });
            return children;
        }
    }
}
