package io.github.springroe.data.mybatis.repository.entity;

import io.github.springroe.data.mybatis.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.data.mybatis.mapping.MybatisPersistentEntity;
import org.springframework.data.mybatis.mapping.MybatisPersistentProperty;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersistentEntityHelper {

    public static MybatisMappingContext mappingContext;

    private final static String NOT_NULL = "%s not allow null";

    public static String getTableName(Class<?> entityClass) {
        MybatisPersistentEntity<?> persistentEntity = persistentEntity(entityClass);
        String name = persistentEntity.getTable().getName().getText();
        return StringUtil.camelToUnderline(name);
    }

    public static <T> MybatisPersistentEntity<T> persistentEntity(Class<T> entityClass) {
        return (MybatisPersistentEntity<T>) mappingContext.getRequiredPersistentEntity(entityClass);
    }

    public static MybatisPersistentProperty getIdProperty(Class<?> entityClass) {
        return persistentEntity(entityClass).getIdProperty();
    }

    public static String getIdColumn(Class<?> entityClass) {
        MybatisPersistentProperty idProperty = getIdProperty(entityClass);
        return getColumn(idProperty);
    }

    public static String getPropertyColumnAlias(Class<?> entityClass, String... propertyPath) {
        String property = propertyPath[0];
        int index = getPropertyIndex(entityClass, property);
        return getPropertyColumnAlias(entityClass, "alias_" + index + "_", 0, propertyPath);
    }

    private static int getPropertyIndex(Class<?> entityClass, String property) {
        Integer index = -1;
        MybatisPersistentEntity<?> persistentEntity = persistentEntity(entityClass);
        MybatisPersistentProperty persistentProperty = persistentEntity.getRequiredPersistentProperty(property);
        if (null == persistentProperty) {
            throw new RuntimeException("Invalid path '" + property);
        }
        List<MybatisPersistentProperty> properties = new ArrayList<>();
        persistentEntity.doWithProperties((PropertyHandler<MybatisPersistentProperty>) properties::add);
        for (MybatisPersistentProperty p : properties) {
            index++;
            if (p.getName().equals(property)) break;
        }
        return index;
    }

    public static String getColumn(Class<?> entityClass, String[] propertyPath) {
        Assert.notEmpty(propertyPath, String.format(NOT_NULL, "propertyPath"));
        MybatisPersistentProperty persistentProperty = null;
        Class<?> nextEntityClass = entityClass;
        for (int i = 0; i < propertyPath.length; i++) {
            String property = propertyPath[i];
            if (i < propertyPath.length - 1) {
                persistentProperty = getPersistentProperty(nextEntityClass, property);
                nextEntityClass = persistentProperty.getActualType();
            } else {
                if (null != persistentProperty) {
                    MybatisPersistentEntity<?> persistentEntity = persistentEntity(nextEntityClass);
                    boolean association = persistentProperty.isAssociation();
                    if (association) {
                        if (property.equals(persistentEntity.getRequiredIdProperty().getName())) {
                            String column = getColumn(persistentProperty);
                            if (StringUtils.isBlank(column)) {
                                throw new RuntimeException("Entity Class '" + entityClass.getSimpleName() + "' Unknown Column Mapping In Path '" + String.join(".", propertyPath) + "'");
                            }
                            return column;
                        }
                    }
                }
                return getColumn(nextEntityClass, property);
            }
        }
        return null;
    }

    public static MybatisPersistentProperty getPersistentProperty(Class<?> entityClass, String property) {
        MybatisPersistentEntity<?> persistentEntity = persistentEntity(entityClass);
        return persistentEntity.getRequiredPersistentProperty(property);
    }

    public static Class<?> getPropertyType(Class<?> entityClass, String[] propertyPath) {
        MybatisPersistentProperty persistentProperty = getPersistentProperty(entityClass, propertyPath);
        return getPropertyType(persistentProperty);
    }

    public static AssociationType associationType(MybatisPersistentProperty property) {
        boolean foundAssociationConf = false;
        Class<? extends Annotation> associationClass = null;
        for (Class<? extends Annotation> clazz : getAssociationAnnotation()) {
            if (property.isAnnotationPresent(clazz)) {
                if (foundAssociationConf) {
                    throw new RuntimeException("There Are Multiple Association Config");
                }
                foundAssociationConf = true;
                associationClass = clazz;
            }
        }
        if (foundAssociationConf) {
            return getAssociationType(associationClass);
        }
        throw new RuntimeException("Association Config Not Found");
    }

    private static AssociationType getAssociationType(Class<? extends Annotation> clazz) {
        AssociationType associationType = null;
        if (OneToOne.class.isAssignableFrom(clazz)) {
            associationType = AssociationType.OneToOne;
        } else if (OneToMany.class.isAssignableFrom(clazz)) {
            associationType = AssociationType.OneToMany;
        } else if (ManyToMany.class.isAssignableFrom(clazz)) {
            associationType = AssociationType.ManyToMany;
        } else if (ManyToOne.class.isAssignableFrom(clazz)) {
            associationType = AssociationType.ManyToOne;
        } else {
            throw new RuntimeException("Unknown Association Type");
        }
        return associationType;
    }

    private static Class<? extends Annotation>[] getAssociationAnnotation() {
        return new Class[]{OneToOne.class, OneToMany.class, ManyToMany.class, ManyToOne.class};
    }

    public static Class<?> getPropertyType(MybatisPersistentProperty persistentProperty) {
        return persistentProperty.isAssociation() ? persistentProperty.getActualType() : persistentProperty.getType();
    }

    public static MybatisPersistentProperty getPersistentProperty(Class<?> entityClass, String[] propertyPath) {
        Assert.notEmpty(propertyPath, String.format(NOT_NULL, "propertyPath"));
        MybatisPersistentProperty persistentProperty = null;
        for (int i = 0; i < propertyPath.length; i++) {
            String property = propertyPath[i];
            persistentProperty = getPersistentProperty(entityClass, property);
            if (null == persistentProperty) {
                break;
            }
            entityClass = persistentProperty.isAssociation() ? persistentProperty.getActualType() : persistentProperty.getType();
        }
        return persistentProperty;
    }

    public static String getColumn(Class<?> entityClass, String property) {
        MybatisPersistentProperty persistentProperty = getPersistentProperty(entityClass, property);
        String column = getColumn(persistentProperty);
        if (StringUtils.isBlank(column)) {
            throw new RuntimeException("Entity Class '" + entityClass.getSimpleName() + "' Unknown Column Mapping For '" + property + "'");
        }
        return column;
    }

    public static String getColumn(MybatisPersistentProperty persistentProperty) {
        if (persistentProperty.isAssociation()) {
            if (persistentProperty.isAnnotationPresent(ManyToOne.class) || persistentProperty.isAnnotationPresent(OneToOne.class)) {
                if (persistentProperty.isAnnotationPresent(JoinColumn.class)) {
                    JoinColumn joinColumn = persistentProperty.getRequiredAnnotation(JoinColumn.class);
                    if (StringUtils.isNotBlank(joinColumn.name())) return joinColumn.name();
                }
            }
        } else return persistentProperty.getColumn().getName().getText();
        return null;
    }

    public static String getPropertyColumnAlias(Class<?> entityClass, String aliasPrefix, int level, String... propertyPath) {
        Assert.notEmpty(propertyPath, String.format(NOT_NULL, "propertyPath"));
        for (int i = 0; i < propertyPath.length; i++) {
            String property = propertyPath[i];
            if (i < propertyPath.length - 1) {
                MybatisPersistentProperty persistentProperty = getPersistentProperty(entityClass, property);
                return getPropertyColumnAlias(persistentProperty.getActualType(), aliasPrefix, ++level, Arrays.copyOfRange(propertyPath, i + 1, propertyPath.length));
            } else {
                String column = getColumn(entityClass, property);
                Integer index = getPropertyIndex(entityClass, property);
                aliasPrefix = aliasPrefix + level + "_" + index + "_" + column;
            }
        }
        return aliasPrefix;
    }

    /**
     * 关联类型
     */
    public enum AssociationType {
        OneToOne {
            @Override
            public AssociationType reverse() {
                return AssociationType.OneToOne;
            }
        },
        OneToMany {
            @Override
            public AssociationType reverse() {
                return AssociationType.ManyToOne;
            }
        },
        ManyToOne {
            @Override
            public AssociationType reverse() {
                return AssociationType.OneToMany;
            }

        },
        ManyToMany {
            @Override
            public AssociationType reverse() {
                return AssociationType.ManyToMany;
            }
        };

        public abstract AssociationType reverse();
    }

}
