package io.github.springroe.data.mybatis.domain.query;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class Conditions {

    private final static String NOT_NULL = "%s not allow null";

    public static Condition and(Condition l, Condition r) {
        Assert.notNull(l, String.format(NOT_NULL, "l"));
        Assert.notNull(r, String.format(NOT_NULL, "r"));
        return new CompoundCondition(Condition.BooleanOperator.AND, new Condition[]{l, r});
    }

    public static Condition and(List<Condition> conditions) {
        Assert.notEmpty(conditions, String.format(NOT_NULL, "conditions"));
        return new CompoundCondition(conditions, Condition.BooleanOperator.AND);
    }

    public static Condition and(Condition... conditions) {
        Assert.notEmpty(conditions, String.format(NOT_NULL, "conditions"));
        return new CompoundCondition(Condition.BooleanOperator.AND, conditions);
    }

    public static Condition or(List<Condition> conditions) {
        Assert.notEmpty(conditions, String.format(NOT_NULL, "conditions"));
        return new CompoundCondition(conditions, Condition.BooleanOperator.OR);
    }

    public static Condition or(Condition... conditions) {
        Assert.notEmpty(conditions, String.format(NOT_NULL, "conditions"));
        return new CompoundCondition(Condition.BooleanOperator.OR, conditions);
    }

    public static Condition equal(String propertyName, Object value) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        Assert.notNull(value, String.format(NOT_NULL, "value"));
        return new ComparisonCondition(propertyName, value, ComparisonCondition.ComparisonOperator.EQUAL);
    }

    public static Condition notEqual(String propertyName, Object value) {
        return equal(propertyName, value).not();
    }

    public static Condition greaterThan(String propertyName, Object value) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        Assert.notNull(value, String.format(NOT_NULL, "value"));
        return new ComparisonCondition(propertyName, value, ComparisonCondition.ComparisonOperator.GREATER_THAN);
    }

    public static Condition greaterThanOrEqual(String propertyName, Object value) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        Assert.notNull(value, String.format(NOT_NULL, "value"));
        return new ComparisonCondition(propertyName, value, ComparisonCondition.ComparisonOperator.GREATER_THAN_OR_EQUAL);
    }

    public static Condition lessThan(String propertyName, Object value) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        Assert.notNull(value, String.format(NOT_NULL, "value"));
        return new ComparisonCondition(propertyName, value, ComparisonCondition.ComparisonOperator.LESS_THAN);
    }

    public static Condition lessThanOrEqual(String propertyName, Object value) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        Assert.notNull(value, String.format(NOT_NULL, "value"));
        return new ComparisonCondition(propertyName, value, ComparisonCondition.ComparisonOperator.LESS_THAN_OR_EQUAL);
    }

    public static Condition isNull(String propertyName) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        return new NullnessCondition(propertyName);
    }

    public static Condition isNotNull(String propertyName) {
        return isNull(propertyName).not();
    }

    public static Condition between(String propertyName, Object lowerBound, Object upperBound) {
        return new BetweenCondition(propertyName, lowerBound, upperBound, null);
    }

    public static Condition between(String propertyName, Object lowerBound, Object upperBound, BetweenCondition.BoundType boundType) {
        Assert.notNull(lowerBound, String.format(NOT_NULL, "lowerBound"));
        Assert.notNull(upperBound, String.format(NOT_NULL, "upperBound"));
        Assert.notNull(boundType, String.format(NOT_NULL, "boundType"));
        return new BetweenCondition(propertyName, lowerBound, upperBound, boundType);
    }

    public static Condition in(String propertyName, List<Object> values) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        Assert.notEmpty(values, String.format(NOT_NULL, "values"));
        return new InCondition(propertyName, values);
    }

    public static Condition in(String propertyName, Object... values) {
        Assert.notEmpty(values, String.format(NOT_NULL, "values"));
        return in(propertyName, Arrays.asList(values));
    }

    public static Condition notIn(String propertyName, List<Object> values) {
        return in(propertyName, values).not();
    }

    public static Condition notIn(String propertyName, Object... values) {
        return in(propertyName, values).not();
    }


    public static Condition like(String propertyName, Object value, MatchMode matchMode) {
        Assert.notNull(propertyName, String.format(NOT_NULL, "propertyName"));
        Assert.notNull(value, String.format(NOT_NULL, "value"));
        Assert.notNull(matchMode, String.format(NOT_NULL, "matchMode"));
        return new LikeCondition(propertyName, value, matchMode);
    }

    public static Condition notLike(String propertyName, Object value, MatchMode matchMode) {
        return like(propertyName, value, matchMode).not();
    }

}
