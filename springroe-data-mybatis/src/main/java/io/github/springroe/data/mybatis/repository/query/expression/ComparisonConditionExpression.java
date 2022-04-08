package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.ComparisonCondition;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class ComparisonConditionExpression extends SimpleConditionExpression {

    private ComparisonCondition condition;

    public ComparisonConditionExpression(ComparisonCondition condition) {
        super(condition);
        this.condition = condition;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        Object value = condition.getValue();
        ComparisonCondition.ComparisonOperator comparisonOperator = condition.getComparisonOperator();
        if (isNegated()) {
            comparisonOperator = comparisonOperator.negated();
        }
        String expressSql = comparisonOperator.rendered();
        String sql = expressSql + String.format(context.getPlaceHolder(), 0);
        return renderMybatisSql(context, sql, value);
    }

}
