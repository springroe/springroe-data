package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.AliasEntry;
import io.github.springroe.data.mybatis.domain.query.PropertyProjection;
import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import io.github.springroe.data.mybatis.repository.entity.PersistentEntityHelper;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mybatis.mapping.MybatisPersistentEntity;
import org.springframework.data.mybatis.mapping.MybatisPersistentProperty;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class PropertyProjectionExpression extends ProjectionExpression {

    private PropertyProjection propertyProjection;

    public PropertyProjectionExpression(PropertyProjection propertyProjection) {
        this.propertyProjection = propertyProjection;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        String propertyName = getPropertyName();
        QueryCondition queryCondition = context.getQueryCondition();
        Class<?> rootEntityClass = getEntityClass(queryCondition);
        String rootAlias = queryCondition.getAlias();
        if (propertyName.contains("*")) {
            Set<String> selectColumnSet = new HashSet<>();
            if (propertyName.equals("*")) {
                List<AliasEntry> aliasEntries = queryCondition.getAliasEntries();
                List<String> selectColumns = selectColumns(queryCondition, rootEntityClass, rootAlias);
                selectColumnSet.addAll(selectColumns);
                for (AliasEntry aliasEntry : aliasEntries) {
                    String alias = aliasEntry.getAlias();
                    String realityPath = getRealityPath(queryCondition, alias);
                    Class<?> entityClass = PersistentEntityHelper.getPropertyType(rootEntityClass, realityPath.split("\\."));
                    List<String> columns = selectColumns(queryCondition, entityClass, alias);
                    selectColumnSet.addAll(columns);
                }
            } else if (propertyName.contains(".")) {
                String alias = getTableAlias(queryCondition, propertyName);
                List<String> columns;
                if (rootAlias.equals(alias)) {
                    columns = selectColumns(queryCondition, rootEntityClass, rootAlias);
                } else {
                    String realityPath = getRealityPath(queryCondition, propertyName.substring(0, propertyName.lastIndexOf('.')));
                    Class<?> entityClass = PersistentEntityHelper.getPropertyType(rootEntityClass, realityPath.split("\\."));
                    columns = selectColumns(queryCondition, entityClass, alias);
                }
                selectColumnSet.addAll(columns);
            }
            return String.join(",", selectColumnSet.toArray(new String[]{}));
        }
        return getFullColumn(queryCondition, propertyName);
    }

    private List<String> selectColumns(QueryCondition queryCondition, Class<?> entityClass, String columnPrefix) {
        List<String> selectColumns = new ArrayList<>();
        MybatisPersistentEntity<?> persistentEntity = PersistentEntityHelper.persistentEntity(entityClass);
        String keyProperty = persistentEntity.getIdProperty().getName();
        columnPrefix = StringUtils.isEmpty(columnPrefix) ? "" : columnPrefix + ".";
        String associationPath = columnPrefix + keyProperty;
        String columnAlias = getPropertyAlias(queryCondition, associationPath);
        String column = getFullColumn(queryCondition, columnPrefix + keyProperty);

        selectColumns.add(column + " AS " + columnAlias);

        List<MybatisPersistentProperty> persistentProperties = new ArrayList<>();

        persistentEntity.doWithProperties((PropertyHandler<MybatisPersistentProperty>) persistentProperty -> persistentProperties.add(persistentProperty));

        for (MybatisPersistentProperty persistentProperty : persistentProperties) {
            if (!persistentProperty.isAssociation()) {
                String property = persistentProperty.getName();
                associationPath = columnPrefix + property;
                columnAlias = getPropertyAlias(queryCondition, associationPath);
                column = getFullColumn(queryCondition, columnPrefix + property);
                selectColumns.add(column + " AS " + columnAlias);
            }
        }
        return selectColumns;
    }

    @Override
    protected String getPropertyName() {
        return propertyProjection.getPropertyName();
    }

}
