package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.NullnessCondition;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class NullnessConditionExpression extends SimpleConditionExpression {

    private NullnessCondition condition;

    public NullnessConditionExpression(NullnessCondition condition) {
        super(condition);
        this.condition = condition;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        String expressSql = " IS NULL";
        if (isNegated()) {
            expressSql = " IS NOT NULL";
        }
        return renderMybatisSql(context, expressSql, null);
    }
}
