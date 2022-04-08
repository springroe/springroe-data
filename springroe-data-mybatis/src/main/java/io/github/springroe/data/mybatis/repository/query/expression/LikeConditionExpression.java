package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.LikeCondition;
import io.github.springroe.data.mybatis.domain.query.MatchMode;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class LikeConditionExpression extends SimpleConditionExpression {

    private LikeCondition condition;

    public LikeConditionExpression(LikeCondition condition) {
        super(condition);
        this.condition = condition;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        MatchMode matchMode = condition.getMatchMode();
        Object value = condition.getValue();
        String expressSql = " LIKE ";
        if (isNegated()) {
            expressSql = " NOT LIKE ";
        }
        String concatStr = matchMode.toConcatString(String.format(context.getPlaceHolder(), 0));
        String sql = expressSql + concatStr;
        return renderMybatisSql(context, sql, new Object[]{value});
    }

}
