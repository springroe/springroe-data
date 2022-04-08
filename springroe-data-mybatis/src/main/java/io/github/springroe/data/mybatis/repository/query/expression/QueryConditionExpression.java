package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.AliasEntry;
import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import io.github.springroe.data.mybatis.repository.entity.PersistentEntityHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mybatis.mapping.MybatisPersistentProperty;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class QueryConditionExpression {

    protected ExpressionType expressionType;

    public abstract String interpret(MybatisQueryContext context);

    protected QueryConditionExpression(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    protected String getFullColumn(QueryCondition queryCondition, String associationPath) {
        String column = getColumn(queryCondition, associationPath);
        String alias = getTableAlias(queryCondition, associationPath);
        if (!StringUtils.isEmpty(alias)) {
            column = alias + "." + column;
        }
        return column;
    }

    protected String getTableAlias(QueryCondition queryCondition, String associationPath) {
        String alias = queryCondition.getAlias();
        if (associationPath.contains(".")) {
            String[] associationPaths = associationPath.split("\\.");
            if (isExistAliasEntry(queryCondition.getAliasEntries(), associationPaths[0])) {
                alias = associationPaths[0];
            }
        }
        return alias;
    }

    private void validateAssociationPath(QueryCondition queryCondition, String associationPath) {
        validate(queryCondition, associationPath);
        String[] associationPaths = associationPath.split("\\.");
        String[] realityPath = getRealityPaths(queryCondition, associationPath);
        MybatisPersistentProperty persistentProperty = PersistentEntityHelper.getPersistentProperty(getEntityClass(queryCondition), realityPath);
        boolean association = persistentProperty.isAssociation();
        if (association) {
            throw new RuntimeException("Invalid path '" + associationPaths[associationPaths.length - 1] + "' In '" + associationPath + "'");
        }
    }

    /**
     * 路径校验（公用）
     *
     * @param queryCondition
     * @param associationPath
     */
    protected void validate(QueryCondition queryCondition, String associationPath) {
        String[] associationPaths = associationPath.split("\\.");
        Class<?> entityClass = getEntityClass(queryCondition);
        if (associationPaths.length > 1) {
            String property = associationPaths[associationPaths.length - 1];
            for (int i = 0; i < associationPaths.length - 1; i++) {
                String path = associationPaths[i];
                boolean existAlias = isExistTableAlias(queryCondition, path);
                if (existAlias) {
                    continue;
                } else {
                    if (associationPaths.length - 1 - i > 1) {
                        throw new RuntimeException("You Must Be Create Table Alias '" + path + "'");
                    }
                    if (associationPaths.length - 1 - i == 1) {
                        String[] realityPath = getRealityPaths(queryCondition, String.join(".", ArrayUtils.subarray(associationPaths, 0, i + 1)));
                        MybatisPersistentProperty persistentProperty = PersistentEntityHelper.getPersistentProperty(entityClass, realityPath);
                        MybatisPersistentProperty idProperty = PersistentEntityHelper.getIdProperty(entityClass);
                        boolean association = persistentProperty.isAssociation();
                        if (association) {
                            if (!idProperty.getName().equals(property)) {
                                throw new RuntimeException("You Must Be Create Table Alias '" + path + "'");
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isExistAliasEntry(List<AliasEntry> aliasEntries, String alias) {
        for (AliasEntry aliasEntry : aliasEntries) {
            if (alias.equals(aliasEntry.getAlias())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isExistTableAlias(QueryCondition queryCondition, String alias) {
        String rootAlias = queryCondition.getAlias();
        if (alias.equals(rootAlias)) {
            return true;
        }
        return isExistAliasEntry(queryCondition.getAliasEntries(), alias);
    }

    protected String[] getRealityPaths(QueryCondition queryCondition, String associationPath) {
        return getRealityPath(queryCondition, associationPath).split("\\.");
    }

    protected String getColumn(QueryCondition queryCondition, String associationPath) {
        validateAssociationPath(queryCondition, associationPath);
        Class<?> rootEntityClass = getEntityClass(queryCondition);
        Class<?> entityClass = rootEntityClass;
        String[] realityPath = getRealityPaths(queryCondition, associationPath);
        return PersistentEntityHelper.getColumn(entityClass, realityPath);
    }

    private Class<?> getEntityClass(QueryCondition queryCondition, String associationPath) {
        Class<?> entityClass = getEntityClass(queryCondition);
        if (associationPath.contains(".")) {
            String path = associationPath.split("\\.")[0];
            if (isExistTableAlias(queryCondition, path)) {
                String realityPath = getRealityPath(queryCondition, path);
                if (!StringUtils.isEmpty(realityPath)) {
                    MybatisPersistentProperty persistentProperty = PersistentEntityHelper.getPersistentProperty(entityClass, realityPath);
                    entityClass = PersistentEntityHelper.getPropertyType(persistentProperty);
                }
            }
        }
        return entityClass;
    }

    protected Class<?> getEntityClass(QueryCondition queryCondition) {
        return queryCondition.getRootClass();
    }

    protected String getRealityPath(QueryCondition queryCondition, String[] associationPath) {
        return getRealityPath(queryCondition, String.join(".", associationPath));
    }

    protected String getRealityPath(QueryCondition queryCondition, String associationPath) {
        StringBuilder sb = new StringBuilder();
        if (associationPath.contains(".")) {
            String[] associationPaths = associationPath.split("\\.");
            String property = String.join(".", ArrayUtils.subarray(associationPaths, 1, associationPaths.length));
            String path = getRealityPath(queryCondition, associationPaths[0]);
            if (!StringUtils.isEmpty(path)) {
                sb.append(path + ".");
            }
            sb.append(property);
        } else {
            String alias = queryCondition.getAlias();
            if (!associationPath.equals(alias)) {
                List<AliasEntry> aliasEntries = queryCondition.getAliasEntries();
                boolean isExistAlias = false;
                for (AliasEntry aliasEntry : aliasEntries) {
                    if (aliasEntry.getAlias().equals(associationPath)) {
                        String path = aliasEntry.getAssociationPath();
                        if (path.contains(".")) {
                            path = getRealityPath(queryCondition, path);
                        }
                        sb.append(path);
                        isExistAlias = true;
                        break;
                    }
                }
                if (!isExistAlias) {
                    sb.append(associationPath);
                }
            }
        }
        return sb.toString();
    }

    public static enum ExpressionType {

        ProjectionExpression,

        AliasExpression,

        ConditionExpression,

        TailExpression

    }

}
