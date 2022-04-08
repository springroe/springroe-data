package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.AliasEntry;
import io.github.springroe.data.mybatis.domain.query.JoinType;
import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import io.github.springroe.data.mybatis.repository.entity.PersistentEntityHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mapping.Association;
import org.springframework.data.mybatis.mapping.MybatisPersistentProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class AliasConditionExpression extends QueryConditionExpression {

    private List<AliasEntry> aliasEntries;

    public AliasConditionExpression(List<AliasEntry> aliasEntries) {
        super(ExpressionType.AliasExpression);
        this.aliasEntries = aliasEntries;
    }

    private void validateAliasAssociationPath(QueryCondition queryCondition, String associationPath) {
        validate(queryCondition, associationPath);
        String[] realityPath = getRealityPaths(queryCondition, associationPath);
        MybatisPersistentProperty persistentProperty = PersistentEntityHelper.getPersistentProperty(getEntityClass(queryCondition), realityPath);
        if (!persistentProperty.isAssociation()) {
            throw new RuntimeException("Invalid Alias Path '" + associationPath + "'");
        }
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        QueryCondition queryCondition = context.getQueryCondition();
        String rootAlias = queryCondition.getAlias();
        StringBuilder sb = new StringBuilder();
        sb.append(PersistentEntityHelper.getTableName(getEntityClass(queryCondition)));
        if (StringUtils.isNotBlank(rootAlias)) {
            sb.append(" " + rootAlias);
        }
        if (null != aliasEntries && aliasEntries.size() > 0) {
            if (StringUtils.isBlank(rootAlias)) {
                throw new RuntimeException("Uninitialized Entity '" + getEntityClass(queryCondition).getSimpleName() + "' Root Alias");
            }
            for (AliasEntry aliasEntry : aliasEntries) {
                sb.append(" " + buildJoin(queryCondition, aliasEntry));
            }
        }
        String sql = sb.toString();
        context.appendFromFragment(sql);
        return sql;
    }

    private String buildJoin(QueryCondition queryCondition, AliasEntry aliasEntry) {
        Class<?> fromEntityClass = getEntityClass(queryCondition);
        String fromEntityAlias = queryCondition.getAlias();

        String associationPath = aliasEntry.getAssociationPath();
        String alias = aliasEntry.getAlias();
        JoinType joinType = aliasEntry.getJoinType();

        validateAliasAssociationPath(queryCondition, associationPath);

        String[] associationPaths = associationPath.split("\\.");

        String[] realityPaths = getRealityPaths(queryCondition, associationPath);

        MybatisPersistentProperty joinPersistentProperty = PersistentEntityHelper.getPersistentProperty(getEntityClass(queryCondition), realityPaths);

        if (associationPaths.length > 1) {
            String[] fromRealityPaths = getRealityPath(queryCondition, ArrayUtils.subarray(associationPaths, 0, associationPaths.length - 1)).split("\\.");
            Class<?> entityClass = PersistentEntityHelper.getPropertyType(fromEntityClass, fromRealityPaths);
            if (null != entityClass) {
                fromEntityClass = entityClass;
                fromEntityAlias = associationPaths[0];
            }
        }
        return buildJoin(fromEntityClass, fromEntityAlias,
                PersistentEntityHelper.getPropertyType(joinPersistentProperty), alias,
                joinType, joinPersistentProperty.getAssociation());
    }

    private String buildJoin(Class<?> fromEntityClass, String fromEntityAlias,
                             Class<?> joinEntityClass, String joinAlias,
                             JoinType joinType, Association<MybatisPersistentProperty> association) {
        return buildJoin(fromEntityClass, fromEntityAlias,
                joinEntityClass, joinAlias,
                joinType, association, false);
    }

    private String getMappedBy(Association<MybatisPersistentProperty> association) {
        String mappedBy = null;
        MybatisPersistentProperty property = association.getInverse();
        PersistentEntityHelper.AssociationType associationType = PersistentEntityHelper.associationType(property);
        if (associationType == PersistentEntityHelper.AssociationType.OneToOne) {
            mappedBy = property.getRequiredAnnotation(OneToOne.class).mappedBy();
        } else if (associationType == PersistentEntityHelper.AssociationType.OneToMany) {
            mappedBy = property.getRequiredAnnotation(OneToMany.class).mappedBy();
        } else if (associationType == PersistentEntityHelper.AssociationType.ManyToMany) {
            mappedBy = property.getRequiredAnnotation(ManyToMany.class).mappedBy();
        }
        return mappedBy;
    }

    private String buildJoin(Class<?> fromEntityClass, String fromEntityAlias,
                             Class<?> joinEntityClass, String joinAlias,
                             JoinType joinType, Association<MybatisPersistentProperty> association, boolean reverse) {
        StringBuilder sb = new StringBuilder();
        String mappedBy = getMappedBy(association);
        String originEntityAlias = reverse ? joinAlias : fromEntityAlias;
        String targetEntityAlias = reverse ? fromEntityAlias : joinAlias;
        Class<?> targetEntityClass = reverse ? fromEntityClass : joinEntityClass;
        MybatisPersistentProperty associationProperty = association.getInverse();
        PersistentEntityHelper.AssociationType associationType = PersistentEntityHelper.associationType(associationProperty);
        if (StringUtils.isBlank(mappedBy)) {
            if (associationType != PersistentEntityHelper.AssociationType.ManyToMany) {
                sb.append(joinType.getJoinType() + " " + PersistentEntityHelper.getTableName(joinEntityClass) + " " + joinAlias);
                sb.append(" ON ");
                List<JoinColumn> joinColumnList = new ArrayList<>();
                if (associationProperty.isAnnotationPresent(JoinColumn.class)) {
                    joinColumnList.add(associationProperty.getRequiredAnnotation(JoinColumn.class));
                }
                if (associationProperty.isAnnotationPresent(JoinColumns.class)) {
                    JoinColumns joinColumns = associationProperty.getRequiredAnnotation(JoinColumns.class);
                    joinColumnList.addAll(Arrays.asList(joinColumns.value()));
                }
                for (int i = 0; i < joinColumnList.size(); i++) {
                    if (i > 0) {
                        sb.append(" AND ");
                    }
                    JoinColumn column = joinColumnList.get(i);
                    String referencedColumnName = StringUtils.isBlank(column.referencedColumnName()) ? PersistentEntityHelper.getIdColumn(targetEntityClass) : column.referencedColumnName();
                    sb.append(originEntityAlias + "." + column.name() + " = " + targetEntityAlias + "." + referencedColumnName);
                }
            } else {
                JoinTable joinTable = associationProperty.getRequiredAnnotation(JoinTable.class);
                String joinTableAlias = PersistentEntityHelper.getTableName(fromEntityClass) + "_" + PersistentEntityHelper.getTableName(joinEntityClass) + "_" + joinAlias;
                sb.append(joinType.getJoinType() + " " + joinTable.name() + " " + joinTableAlias);
                sb.append(" ON ");
                JoinColumn[] joinColumns = reverse ? joinTable.inverseJoinColumns() : joinTable.joinColumns();
                JoinColumn[] inverseJoinColumns = reverse ? joinTable.joinColumns() : joinTable.inverseJoinColumns();
                for (int i = 0; i < joinColumns.length; i++) {
                    if (i > 0) {
                        sb.append(" AND ");
                    }
                    JoinColumn joinColumn = joinColumns[i];
                    String referencedColumnName = StringUtils.isBlank(joinColumn.referencedColumnName()) ? PersistentEntityHelper.getTableName(fromEntityClass) + "_" + PersistentEntityHelper.getIdColumn(fromEntityClass) : joinColumn.referencedColumnName();
                    sb.append(fromEntityAlias + "." + joinColumn.name() + " = " + joinTableAlias + "." + referencedColumnName);
                }
                sb.append(" " + joinType.getJoinType() + " " + PersistentEntityHelper.getTableName(joinEntityClass) + " " + joinAlias);
                sb.append(" ON ");
                for (int i = 0; i < inverseJoinColumns.length; i++) {
                    if (i > 0) {
                        sb.append(" AND ");
                    }
                    JoinColumn joinColumn = inverseJoinColumns[i];
                    String referencedColumnName = StringUtils.isBlank(joinColumn.referencedColumnName()) ? PersistentEntityHelper.getTableName(joinEntityClass) + "_" + PersistentEntityHelper.getIdColumn(joinEntityClass) : joinColumn.referencedColumnName();
                    sb.append(joinAlias + "." + joinColumn.name() + " = " + joinTableAlias + "." + referencedColumnName);
                }
            }
        } else {
            MybatisPersistentProperty persistentProperty = PersistentEntityHelper.getPersistentProperty(joinEntityClass, mappedBy);
            if (!persistentProperty.isAssociation()) {
                throw new RuntimeException("Entity '" + joinEntityClass.getSimpleName() + "', Property '" + mappedBy + "' Does Not Exist Association Config");
            }

            Class<?> propertyType = PersistentEntityHelper.getPropertyType(persistentProperty);
            if (fromEntityClass != propertyType) {
                throw new RuntimeException("Entity '" + fromEntityClass + "', Property '" + mappedBy + "' Invalid Association Config");
            }
            Association<MybatisPersistentProperty> propertyAssociation = persistentProperty.getAssociation();
            PersistentEntityHelper.AssociationType joinAssociationType = PersistentEntityHelper.associationType(propertyAssociation.getInverse());
            if (associationType.reverse() != joinAssociationType) {
                throw new RuntimeException("Entity '" + joinEntityClass.getSimpleName() + "', Property '" + mappedBy + "' Must Be '" + associationType.reverse() + "' Association");
            }
            String joinSql = buildJoin(fromEntityClass, fromEntityAlias, joinEntityClass, joinAlias, joinType, propertyAssociation, true);
            sb.append(joinSql);
        }
        return sb.toString();
    }

}
