package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.Condition;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class CompoundConditionExpression extends ConditionExpression {

    private List<ConditionExpression> conditionExpressions;

    private Condition.BooleanOperator operator;

    public CompoundConditionExpression(List<ConditionExpression> conditionExpressions, Condition.BooleanOperator operator) {
        this.conditionExpressions = conditionExpressions;
        this.operator = operator;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < conditionExpressions.size(); i++) {
            ConditionExpression conditionExpression = conditionExpressions.get(i);
            String sql = conditionExpression.interpret(context);
            sb.append(sql);
            if (i < conditionExpressions.size() - 1) {
                sb.append(" " + operator + " ");
            }
        }
        sb.append(")");
        String whereFragment = sb.toString();
        context.clearWhereFragment();
        context.appendWhereFragment(whereFragment);
        return whereFragment;
    }

}
