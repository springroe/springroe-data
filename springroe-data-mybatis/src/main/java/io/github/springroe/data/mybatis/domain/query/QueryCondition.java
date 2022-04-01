package io.github.springroe.data.mybatis.domain.query;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public interface QueryCondition {

    static QueryCondition getInstance() {
        return new QueryConditionImpl("root_alias_");
    }

    static QueryCondition forAlias(String alias) {
        return new QueryConditionImpl(alias);
    }

    QueryCondition add(Condition condition);

    QueryCondition and(Condition... conditions);

    QueryCondition and(List<Condition> conditions);

    QueryCondition and(Condition l, Condition r);

    QueryCondition or(Condition... conditions);

    QueryCondition or(List<Condition> conditions);

    QueryCondition equal(String propertyName, Object value);

    QueryCondition notEqual(String propertyName, Object value);

    QueryCondition greaterThan(String propertyName, Object value);

    QueryCondition greaterThanOrEqual(String propertyName, Object value);

    QueryCondition lessThan(String propertyName, Object value);

    QueryCondition lessThanOrEqual(String propertyName, Object value);

    QueryCondition isNull(String propertyName);

    QueryCondition isNotNull(String propertyName);

    QueryCondition between(String propertyName, Object lowerBound, Object upperBound, BetweenCondition.BoundType boundType);

    QueryCondition in(String propertyName, List<Object> values);

    QueryCondition notIn(String propertyName, List<Object> values);

    QueryCondition notIn(String propertyName, Object... values);

    QueryCondition like(String propertyName, Object value, MatchMode matchMode);

    QueryCondition notLike(String propertyName, Object value, MatchMode matchMode);

    QueryCondition createAlias(String associationPath, String alias);

    QueryCondition createAlias(String associationPath, String alias, JoinType joinType);

    QueryCondition addOrder(Order... orders);

    QueryCondition groupBy(String... associationPaths);

    String getAlias();

    List<QueryConditionImpl.ConditionEntry> getConditionEntries();

    List<QueryConditionImpl.OrderEntry> getOrderEntries();

    List<String> getGroupBys();

    List<AliasEntry> getAliasEntries();

    Map<String, JoinType> getFetchModes();

    QueryCondition setFetchMode(String associationPath, JoinType joinType);

    Projection getProjection();

    void setProjection(Projection projection);

    QueryCondition distinct(boolean distinct);

    boolean isDistinct();

}
