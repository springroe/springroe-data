package io.github.springroe.data.mybatis.repository.query.expression;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class ConditionExpression extends QueryConditionExpression {

    private boolean negated;

    public ConditionExpression() {
        super(ExpressionType.ConditionExpression);
    }

    public ConditionExpression not() {
        this.negated = !negated;
        return this;
    }

    @Override
    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public boolean isNegated() {
        return negated;
    }

}
