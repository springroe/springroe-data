package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import io.github.springroe.data.mybatis.repository.entity.PersistentEntityHelper;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class ProjectionExpression extends QueryConditionExpression {

    public ProjectionExpression() {
        super(ExpressionType.ProjectionExpression);
    }

    protected String getPropertyName() {
        return null;
    }

    public String getPropertyAlias(QueryCondition queryCondition, String associationPath) {
        Class entityClass = getEntityClass(queryCondition);
        String[] realityPath = getRealityPaths(queryCondition, associationPath);
        return PersistentEntityHelper.getPropertyColumnAlias(entityClass, realityPath);
    }

}
