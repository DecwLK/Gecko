package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;

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
    @Setter(onParam_ = @NonNull)
    private Visibility visibility;
    private boolean hasIncomingConnection;

    public static final List<String> BUILTIN_TYPES =
        List.of("int", "int8", "int16", "int32", "int64", "uint", "uint8", "uint16", "uint32", "uint64", "float",
            "double", "short", "long", "bool");

    @JsonCreator
    public Variable(
        @JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") String type,
        @JsonProperty("visibility") Visibility visibility) throws ModelException {
        super(id);
        setVisibility(visibility);
        setName(name);
        setType(type);
    }

    @Override
    public void setName(@NonNull String name) throws ModelException {
        if (name.isEmpty()) {
            throw new ModelException("Variable's name is invalid.");
        }
        this.name = name;
    }

    public void setType(@NonNull String type) throws ModelException {
        if (type.isEmpty()) {
            throw new ModelException("Variable's type is invalid.");
        }
        this.type = type;
    }

    @JsonIgnore
    public static List<String> getBuiltinTypes() {
        return BUILTIN_TYPES;
    }

    public void setValue(String value) throws ModelException {
        if (value != null && value.isEmpty()) {
            throw new ModelException("Variable's value is invalid.");
        }
        this.value = value;
    }

    @Override
    public void accept(ElementVisitor visitor) throws ModelException, MissingViewModelElementException {
        visitor.visit(this);
    }
}
