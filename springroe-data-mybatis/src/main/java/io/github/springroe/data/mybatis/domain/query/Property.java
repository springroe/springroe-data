package io.github.springroe.data.mybatis.domain.query;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class Property extends PropertyProjection {

    public Property(String propertyName) {
        super(propertyName);
    }

    public static Projection forName(String propertyName) {
        return new Property(propertyName);
    }

    public Condition between(Object min, Object max, BetweenCondition.BoundType boundType) {
        return Conditions.between(this.getPropertyName(), min, max, boundType);
    }

    public Condition equal(Object value) {
        return Conditions.equal(this.getPropertyName(), value);
    }

    public Condition notEqual(Object value) {
        return Conditions.notEqual(this.getPropertyName(), value);
    }

    public Condition greaterThan(Object value) {
        return Conditions.greaterThan(this.getPropertyName(), value);
    }

    public Condition greaterThanOrEqual(Object value) {
        return Conditions.greaterThanOrEqual(this.getPropertyName(), value);
    }

    public Condition lessThan(Object value) {
        return Conditions.lessThan(this.getPropertyName(), value);
    }

    public Condition lessThanOrEqual(Object value) {
        return Conditions.lessThanOrEqual(this.getPropertyName(), value);
    }

    public Condition in(Object... values) {
        return Conditions.in(this.getPropertyName(), values);
    }

    public Condition in(List<Object> values) {
        return Conditions.in(this.getPropertyName(), values);
    }

    public Condition notIn(Object... values) {
        return Conditions.notIn(this.getPropertyName(), values);
    }

    public Condition notIn(List<Object> values) {
        return Conditions.notIn(this.getPropertyName(), values);
    }

    public Condition isNull() {
        return Conditions.isNull(this.getPropertyName());
    }

    public Condition isNotNull() {
        return Conditions.isNotNull(this.getPropertyName());
    }

    public Condition like(Object value, MatchMode matchMode) {
        return Conditions.like(this.getPropertyName(), value, matchMode);
    }

    public Condition notLike(Object value, MatchMode matchMode) {
        return Conditions.notLike(this.getPropertyName(), value, matchMode);
    }

    public Order asc() {
        return Order.asc(this.getPropertyName());
    }

    public Order desc() {
        return Order.desc(this.getPropertyName());
    }

}
