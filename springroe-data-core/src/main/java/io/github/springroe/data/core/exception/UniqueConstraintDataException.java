package io.github.springroe.data.core.exception;

import lombok.Getter;
import lombok.NonNull;

public class UniqueConstraintDataException extends DataException {

    @NonNull
    @Getter
    private final Class<?> entityClass;
    @NonNull
    @Getter
    private final String property;

    public UniqueConstraintDataException( @NonNull Class<?> entityClass, @NonNull String property) {
        super("UniqueConstraintDataException");
        this.entityClass = entityClass;
        this.property = property;
    }


    @Override
    public String toString() {
        return "UniqueConstraintDataException{" +
                "entityClass=" + entityClass +
                ", property='" + property + '\'' +
                '}';
    }
}
