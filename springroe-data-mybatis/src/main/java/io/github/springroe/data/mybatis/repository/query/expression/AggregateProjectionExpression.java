package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.AggregateProjection;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class AggregateProjectionExpression extends ProjectionExpression {

    private AggregateProjection projection;

    public AggregateProjectionExpression(AggregateProjection projection) {
        this.projection = projection;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        String function = projection.getFunction().toString();
        String propertyName = projection.getPropertyName();
        return function + "(" + getFullColumn(context.getQueryCondition(), propertyName) + ")";
    }

    @Override
    protected String getPropertyName() {
        return projection.getPropertyName();
    }
}
