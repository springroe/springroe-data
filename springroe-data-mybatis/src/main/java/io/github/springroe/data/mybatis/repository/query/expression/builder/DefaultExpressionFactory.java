package io.github.springroe.data.mybatis.repository.query.expression.builder;

import io.github.springroe.data.mybatis.domain.query.*;
import io.github.springroe.data.mybatis.repository.query.expression.QueryConditionExpression;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class DefaultExpressionFactory implements ExpressionFactory {

    @Override
    public QueryConditionExpression create(Condition condition) {
        Assert.notNull(condition, String.format(NOT_NULL, "condition"));
        return QueryExpressionBuilder.build(condition);
    }

    @Override
    public QueryConditionExpression create(Condition... conditions) {
        Assert.notEmpty(conditions, String.format(NOT_NULL, "conditions"));
        Condition condition = Conditions.and(conditions);
        return create(condition);
    }

    @Override
    public QueryConditionExpression create(List<AliasEntry> aliasEntries) {
        Assert.notEmpty(aliasEntries, String.format(NOT_NULL, "aliasEntries"));
        return QueryExpressionBuilder.build(aliasEntries);
    }

    @Override
    public QueryConditionExpression create(Projection projection) {
        Assert.notNull(projection, String.format(NOT_NULL, "projection"));
        return QueryExpressionBuilder.build(Projections.projectionList().addProjection(projection));
    }

    @Override
    public QueryConditionExpression create(List<Order> orders, List<String> groupBy) {
        Assert.isTrue((null != orders && orders.size() > 0)
                || (null != groupBy && groupBy.size() > 0), "One Must Not Be Empty");
        return QueryExpressionBuilder.build(orders, groupBy);
    }
}
