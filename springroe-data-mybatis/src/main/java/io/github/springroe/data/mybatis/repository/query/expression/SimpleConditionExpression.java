package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.AbstractSimpleCondition;
import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class SimpleConditionExpression extends ConditionExpression {

    protected AbstractSimpleCondition condition;

    protected SimpleConditionExpression(AbstractSimpleCondition condition) {
        this.condition = condition;
    }

    protected String renderMybatisSql(MybatisQueryContext context, String sql, Object value) {
        return renderMybatisSql(context, sql, new Object[]{value});
    }

    protected String renderMybatisSql(MybatisQueryContext context, String sql, Object[] values) {
        String propertyName = condition.getPropertyName();
        QueryCondition queryCondition = context.getQueryCondition();
        String column = getFullColumn(queryCondition, propertyName);
        sql = column + sql;
        Map<String, Object> paramNameValuePairs = context.getParamNameValuePairs();
        if (ArrayUtils.isNotEmpty(values)) {
            for (int i = 0; i < values.length; ++i) {
                String genParamName = context.getMybatisGeneralParamName() + context.getParamNameSeq().incrementAndGet();
                sql = sql.replace(String.format(context.getPlaceHolder(), i),
                        String.format(context.getMybatisToken(), genParamName));
                paramNameValuePairs.put(genParamName, values[i]);
            }
        }
        return sql;
    }

}
