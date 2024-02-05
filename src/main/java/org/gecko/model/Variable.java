package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a variable in the domain model of a Gecko project. A {@link Variable} has a name, a type and a
 * {@link Visibility}.
 */
@Getter
@Setter
public class Variable extends Element implements Renamable {
    private String name;
    private String type;
    private String value;
    private Visibility visibility;
    private boolean hasIncomingConnection;

    private static final Set<String> BUILTIN_TYPES =
        java.util.Set.of("int", "int8", "int16", "int32", "int64", "uint", "uint8", "uint16", "uint32", "uint64",
            "float", "double", "short", "long", "bool");

    @JsonCreator
    public Variable(
        @JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") String type,
        @JsonProperty("visibility") Visibility visibility) {
        super(id);
        this.visibility = visibility;
        this.name = name;
        this.type = type;
    }

    public static Set<String> getBuiltinTypes() {
        return BUILTIN_TYPES;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
