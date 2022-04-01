package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class Projections {

    public static PropertyProjection property(String propertyName) {
        return new PropertyProjection(propertyName);
    }

    public static Projection alias(Projection projection, String alias) {
        return new AliasedProjection(projection, alias);
    }

    public static AggregateProjection count(String propertyName) {
        return new AggregateProjection(AggregateProjection.Function.Count, propertyName);
    }

    public static AggregateProjection avg(String propertyName) {
        return new AggregateProjection(AggregateProjection.Function.Avg, propertyName);
    }

    public static AggregateProjection max(String propertyName) {
        return new AggregateProjection(AggregateProjection.Function.Max, propertyName);
    }

    public static AggregateProjection min(String propertyName) {
        return new AggregateProjection(AggregateProjection.Function.Min, propertyName);
    }

    public static Projection distinct(Projection projection) {
        return new DistinctProjection(projection);
    }

    public static Projection rowCount() {
        return new RowCountProjection();
    }

    public static ProjectionList projectionList() {
        return new ProjectionList();
    }

}
