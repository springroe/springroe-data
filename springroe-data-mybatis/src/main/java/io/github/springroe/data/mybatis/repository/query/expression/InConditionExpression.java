package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.InCondition;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class InConditionExpression extends SimpleConditionExpression {

    private InCondition condition;

    public InConditionExpression(InCondition condition) {
        super(condition);
        this.condition = condition;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        List<Object> values = condition.getValues();
        StringBuilder sb = new StringBuilder(" IN (");
        if (isNegated()) {
            sb = new StringBuilder(" NOT IN (");
        }
        for (int i = 0; i < values.size(); i++) {
            sb.append(String.format(context.getPlaceHolder(), i));
            if (i < values.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        String sql = sb.toString();
        return renderMybatisSql(context, sql, values.toArray());
    }
}
