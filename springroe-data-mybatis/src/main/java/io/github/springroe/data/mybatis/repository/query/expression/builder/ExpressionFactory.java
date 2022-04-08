package io.github.springroe.data.mybatis.repository.query.expression.builder;

import io.github.springroe.data.mybatis.domain.query.*;
import io.github.springroe.data.mybatis.repository.query.expression.AliasConditionExpression;
import io.github.springroe.data.mybatis.repository.query.expression.QueryConditionExpression;
import io.github.springroe.data.mybatis.repository.query.expression.TailConditionExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 解析器工厂
 * @author: tl
 * @date: 2020/6/12
 */
public interface ExpressionFactory {

    String NOT_NULL = "%s not allow null";

    QueryConditionExpression create(Condition condition);

    QueryConditionExpression create(Condition... conditions);

    QueryConditionExpression create(List<AliasEntry> aliasEntries);

    QueryConditionExpression create(Projection projection);

    QueryConditionExpression create(List<Order> orders, List<String> groupBy);

    static QueryConditionExpression create(QueryCondition queryCondition, QueryExpressionType expressionType) {
        QueryConditionExpression queryConditionExpression = null;
        switch (expressionType) {
            case ProjectionExpression:
                Projection projection = queryCondition.getProjection();
                if (null == projection) {
                    projection = Projections.property("*");
                }
                queryConditionExpression = QueryExpressionBuilder.build(Projections.projectionList().addProjection(projection));
                break;
            case AliasExpression:
                List<AliasEntry> aliasEntries = queryCondition.getAliasEntries();
                queryConditionExpression = new AliasConditionExpression(aliasEntries);
                break;
            case ConditionExpression:
                List<QueryConditionImpl.ConditionEntry> conditionEntries = queryCondition.getConditionEntries();
                if (null != conditionEntries && conditionEntries.size() > 0) {
                    List<Condition> conditions = new ArrayList<>();
                    for (QueryConditionImpl.ConditionEntry conditionEntry : conditionEntries) {
                        conditions.add(conditionEntry.getCondition());
                    }
                    Condition condition = Conditions.and(conditions);
                    queryConditionExpression = QueryExpressionBuilder.build(condition);
                }
                break;
            case TailExpression:
                List<QueryConditionImpl.OrderEntry> orderEntries = queryCondition.getOrderEntries();
                List<String> groupBys = queryCondition.getGroupBys();
                List<Order> orders = new ArrayList<>();
                if ((null != orderEntries && orderEntries.size() > 0) || (null != groupBys && groupBys.size() > 0)) {
                    for (QueryConditionImpl.OrderEntry orderEntry : orderEntries) {
                        orders.add(orderEntry.getOrder());
                    }
                }
                queryConditionExpression = new TailConditionExpression(orders, groupBys);
                break;
        }
        return queryConditionExpression;
    }

    /**
     * 查询解析器类型
     */
    enum QueryExpressionType {
        ProjectionExpression,
        AliasExpression,
        ConditionExpression,
        TailExpression
    }

}
