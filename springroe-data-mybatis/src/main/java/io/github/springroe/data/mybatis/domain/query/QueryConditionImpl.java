package io.github.springroe.data.mybatis.domain.query;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class QueryConditionImpl implements QueryCondition {

    private final static String NOT_NULL = "%s not allow null";

    private final String alias;

    private final List<ConditionEntry> conditionEntries;

    private final List<OrderEntry> orderEntries;

    private final List<AliasEntry> aliasEntries;

    private final Map<String, JoinType> fetchModes;

    private Projection projection;

    private boolean distinct;

    private final List<String> groupBy;

    protected QueryConditionImpl(String alias) {
        this.alias = alias;
        this.conditionEntries = new ArrayList<>();
        this.aliasEntries = new ArrayList<>();
        this.orderEntries = new ArrayList<>();
        this.fetchModes = new HashMap<>();
        this.groupBy = new ArrayList<>();
    }

    @Override
    public QueryCondition add(Condition condition) {
        this.conditionEntries.add(new ConditionEntry(this, condition));
        return this;
    }

    @Override
    public QueryCondition and(Condition... conditions) {
        Condition condition = Conditions.and(conditions);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition and(List<Condition> conditions) {
        Condition condition = Conditions.and(conditions);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition and(Condition l, Condition r) {
        Condition condition = Conditions.and(l, r);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition or(Condition... conditions) {
        Condition condition = Conditions.or(conditions);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition or(List<Condition> conditions) {
        Condition condition = Conditions.or(conditions);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition equal(String propertyName, Object value) {
        Condition condition = Conditions.equal(propertyName, value);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition notEqual(String propertyName, Object value) {
        Condition condition = Conditions.notEqual(propertyName, value);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition greaterThan(String propertyName, Object value) {
        Condition condition = Conditions.greaterThan(propertyName, value);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition greaterThanOrEqual(String propertyName, Object value) {
        Condition condition = Conditions.greaterThanOrEqual(propertyName, value);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition lessThan(String propertyName, Object value) {
        Condition condition = Conditions.lessThan(propertyName, value);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition lessThanOrEqual(String propertyName, Object value) {
        Condition condition = Conditions.lessThanOrEqual(propertyName, value);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition isNull(String propertyName) {
        Condition condition = Conditions.isNull(propertyName);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition isNotNull(String propertyName) {
        Condition condition = Conditions.isNotNull(propertyName);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition between(String propertyName, Object lowerBound, Object upperBound, BetweenCondition.BoundType boundType) {
        Condition condition = Conditions.between(propertyName, lowerBound, upperBound, boundType);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition in(String propertyName, List<Object> values) {
        Condition condition = Conditions.in(propertyName, values);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition notIn(String propertyName, List<Object> values) {
        Condition condition = Conditions.notIn(propertyName, values);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition notIn(String propertyName, Object... values) {
        Condition condition = Conditions.notIn(propertyName, values);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition like(String propertyName, Object value, MatchMode matchMode) {
        Condition condition = Conditions.like(propertyName, value, matchMode);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition notLike(String propertyName, Object value, MatchMode matchMode) {
        Condition condition = Conditions.notLike(propertyName, value, matchMode);
        this.add(condition);
        return this;
    }

    @Override
    public QueryCondition createAlias(String associationPath, String alias) {
        this.createAlias(associationPath, alias, JoinType.INNER_JOIN);
        return this;
    }

    @Override
    public QueryCondition createAlias(String associationPath, String alias, JoinType joinType) {
        AliasEntry aliasEntry = new AliasEntry(associationPath, alias, joinType);
        checkAlias(aliasEntry);
        this.aliasEntries.add(aliasEntry);
        return this;
    }

    @Override
    public QueryCondition addOrder(Order... orders) {
        Assert.notEmpty(orders, String.format(NOT_NULL, "orders"));
        for (Order order : orders) {
            String orderBy = order.getOrderBy();
            this.orderEntries.add(new OrderEntry(this, order));
        }
        return this;
    }

    @Override
    public QueryCondition groupBy(String... associationPaths) {
        Assert.notEmpty(associationPaths, String.format(NOT_NULL, "groupBy"));
        for (String associationPath : associationPaths) {
            this.groupBy.add(associationPath);
        }
        return this;
    }

    private void checkAlias(AliasEntry aliasEntry) {
        String associationPath = aliasEntry.getAssociationPath();
        String alias = aliasEntry.getAlias();
        Assert.notNull(alias, String.format(NOT_NULL, "alias"));
        if (null != this.alias && this.alias.equals(alias)) {
            throw new IllegalArgumentException("Cannot be the same as the root alias '" + alias + "'");
        }
        for (AliasEntry entry : aliasEntries) {
            if (entry.getAlias().equals(alias)) {
                throw new IllegalArgumentException("The same alias already exists '" + alias + "'");
            }
        }
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public List<ConditionEntry> getConditionEntries() {
        return conditionEntries;
    }

    @Override
    public List<OrderEntry> getOrderEntries() {
        return orderEntries;
    }

    @Override
    public List<String> getGroupBys() {
        return this.groupBy;
    }

    @Override
    public List<AliasEntry> getAliasEntries() {
        return aliasEntries;
    }

    @Override
    public Map<String, JoinType> getFetchModes() {
        return fetchModes;
    }

    @Override
    public QueryCondition setFetchMode(String associationPath, JoinType joinType) {
        Assert.notNull(joinType, String.format(NOT_NULL, "JoinType"));
        this.fetchModes.put(associationPath, joinType);
        return this;
    }

    @Override
    public Projection getProjection() {
        return projection;
    }

    @Override
    public void setProjection(Projection projection) {
        Assert.notNull(projection, String.format(NOT_NULL, "projection"));
        this.projection = projection;
    }

    @Override
    public QueryCondition distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    @Override
    public boolean isDistinct() {
        return this.distinct;
    }

    public class ConditionEntry {

        private QueryCondition queryCondition;

        private Condition condition;

        private ConditionEntry(QueryCondition queryCondition, Condition condition) {
            this.queryCondition = queryCondition;
            this.condition = condition;
        }

        public QueryCondition getQueryCondition() {
            return queryCondition;
        }

        public void setQueryCondition(QueryCondition queryCondition) {
            this.queryCondition = queryCondition;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }

    public class OrderEntry {

        private QueryCondition queryCondition;

        private Order order;

        private OrderEntry(QueryCondition queryCondition, Order order) {
            this.queryCondition = queryCondition;
            this.order = order;
        }

        public QueryCondition getQueryCondition() {
            return queryCondition;
        }

        public void setQueryCondition(QueryCondition queryCondition) {
            this.queryCondition = queryCondition;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }
    }

}
