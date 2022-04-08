package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.BetweenCondition;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class BetweenConditionExpression extends SimpleConditionExpression {

    private BetweenCondition condition;

    public BetweenConditionExpression(BetweenCondition condition) {
        super(condition);
        this.condition = condition;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        Object lowerBound = condition.getLowerBound();
        Object upperBound = condition.getUpperBound();
        String expressSql = " BETWEEN ";
        if (isNegated()) {
            expressSql = " NOT BETWEEN ";
        }
        String sql = expressSql + String.format(context.getPlaceHolder(), 0) + " AND " + String.format(context.getPlaceHolder(), 1);
        return renderMybatisSql(context, sql, new Object[]{lowerBound, upperBound});
    }

}
