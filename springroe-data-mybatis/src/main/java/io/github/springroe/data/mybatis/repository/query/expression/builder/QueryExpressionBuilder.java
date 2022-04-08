package io.github.springroe.data.mybatis.repository.query.expression.builder;

import io.github.springroe.data.mybatis.domain.query.AliasEntry;
import io.github.springroe.data.mybatis.domain.query.Condition;
import io.github.springroe.data.mybatis.domain.query.Order;
import io.github.springroe.data.mybatis.domain.query.Projection;
import io.github.springroe.data.mybatis.repository.query.expression.AliasConditionExpression;
import io.github.springroe.data.mybatis.repository.query.expression.QueryConditionExpression;
import io.github.springroe.data.mybatis.repository.query.expression.TailConditionExpression;

import java.util.List;

/**
 * @description: 查询解析器建造器
 * @author: tl
 * @date: 2020/6/12
 */
public class QueryExpressionBuilder {

    public static QueryConditionExpression build(Condition condition) {
        return new ConditionExpressionBuilder(condition).build();
    }

    public static QueryConditionExpression build(Projection projection) {
        return new ProjectionExpressionBuilder(projection).build();
    }

    public static QueryConditionExpression build(List<AliasEntry> aliasEntries) {
        return new AliasConditionExpression(aliasEntries);
    }

    public static QueryConditionExpression build(List<Order> orders, List<String> groupBy) {
        return new TailConditionExpression(orders, groupBy);
    }

}
