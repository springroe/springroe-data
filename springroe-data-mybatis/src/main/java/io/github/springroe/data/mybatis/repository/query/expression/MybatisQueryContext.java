package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import io.github.springroe.data.mybatis.domain.query.QueryConditionImpl;
import io.github.springroe.data.mybatis.repository.query.expression.builder.ExpressionFactory;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: mybatis 查询上下文
 * @author: tl
 * @date: 2020/6/12
 */
@Data
public class MybatisQueryContext {

    private final AtomicInteger paramNameSeq = new AtomicInteger(0);

    private final static String MYBATIS_TOKEN = "#{paramNameValuePairs.%s}";

    private final static String MYBATIS_GENERAL_PARAMNAME = "MYBATISGENVAL";

    protected final static String PLACE_HOLDER = "{%s}";

    private final Map<String, Object> paramNameValuePairs = new HashMap<>();

    private QueryCondition queryCondition;

    private final StringBuilder
            selectFragment,
            fromFragment,
            whereFragment,
            orderByFragment,
            groupByFragment;

    public MybatisQueryContext(QueryCondition queryCondition) {
        this.queryCondition = queryCondition;
        this.selectFragment = new StringBuilder();
        this.fromFragment = new StringBuilder();
        this.whereFragment = new StringBuilder();
        this.orderByFragment = new StringBuilder();
        this.groupByFragment = new StringBuilder();
    }

    public String getMybatisGeneralParamName() {
        return MYBATIS_GENERAL_PARAMNAME;
    }

    public String getMybatisToken() {
        return MYBATIS_TOKEN;
    }

    public String getPlaceHolder() {
        return PLACE_HOLDER;
    }

    public static MybatisQueryContext explain(QueryCondition queryCondition) {
        MybatisQueryContext context = new MybatisQueryContext(queryCondition);
        explain(context, ExpressionFactory.QueryExpressionType.AliasExpression);
        explain(context, ExpressionFactory.QueryExpressionType.ProjectionExpression);
        explain(context, ExpressionFactory.QueryExpressionType.ConditionExpression);
        explain(context, ExpressionFactory.QueryExpressionType.TailExpression);
        return context;
    }

    public static void explain(MybatisQueryContext context, ExpressionFactory.QueryExpressionType queryExpressionType) {
        QueryConditionExpression queryConditionExpression = ExpressionFactory.create(context.getQueryCondition(), queryExpressionType);
        if (null != queryConditionExpression) {
            queryConditionExpression.interpret(context);
        }
    }

    public Map<String, Object> getParamNameValuePairs() {
        return paramNameValuePairs;
    }

    public QueryCondition getQueryCondition() {
        return queryCondition;
    }

    public String getSelectFragment() {
        return selectFragment.toString();
    }

    public String getFromFragment() {
        return fromFragment.toString();
    }

    public String getWhereFragment() {
        return whereFragment.toString();
    }

    public void clearWhereFragment() {
        whereFragment.delete(0, whereFragment.length());
    }

    public void appendWhereFragment(String whereFragment) {
        this.whereFragment.append(whereFragment);
    }

    public void appendSelectFragment(String selectFragment) {
        this.selectFragment.append(selectFragment);
    }

    public void appendOrderByFragment(String orderByFragment) {
        this.orderByFragment.append(orderByFragment);
    }

    public void appendGroupByFragment(String groupByFragment) {
        this.groupByFragment.append(groupByFragment);
    }

    public void appendFromFragment(String fromFragment) {
        this.fromFragment.append(fromFragment);
    }

    public String getOrderByFragment() {
        return orderByFragment.toString();
    }

    public String getGroupByFragment() {
        return groupByFragment.toString();
    }

    public Map<String, Object> fragment() {
        Map<String, Object> map = new HashMap<>();
        map.put("selectFragment", getSelectFragment());
        map.put("fromFragment", getFromFragment());
        map.put("whereFragment", getWhereFragment());
        map.put("groupByFragment", getGroupByFragment());
        map.put("orderByFragment", getOrderByFragment());
        map.put("paramNameValuePairs", paramNameValuePairs);
        return map;
    }

    @Override
    public String toString() {
        return "{" +
                "selectFragment=" + selectFragment +
                ", fromFragment=" + fromFragment +
                ", whereFragment=" + whereFragment +
                ", orderByFragment=" + orderByFragment +
                ", groupByFragment=" + groupByFragment +
                '}';
    }
}
