package io.github.springroe.data.jpa.domain;

import io.github.springroe.data.core.domain.Persistable;
import lombok.Data;
import org.hibernate.metamodel.model.domain.internal.AbstractPluralAttribute;

import jakarta.persistence.OneToMany;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Data
public final class OneToManyAttributeData {
    private final AbstractPluralAttribute<?, ?, ?> attribute;
    private final OneToMany oneToMany;
    private final Class<?> toManyClass;
    private final Field toManyField;
    private final Field toOneField;
    private Map<Object, ? extends List<? extends Persistable<?>>> data;
}
