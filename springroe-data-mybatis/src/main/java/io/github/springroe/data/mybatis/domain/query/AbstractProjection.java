package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class AbstractProjection implements Projection {

    protected ProjectionType projectionType;

    protected AbstractProjection(ProjectionType projectionType) {
        this.projectionType = projectionType;
    }

    @Override
    public ProjectionType getProjectionType() {
        return projectionType;
    }

    public static enum ProjectionType {
        PropertyProjection,
        CompoundProjection,
        AggregateProjection,
        AliasedProjection,
        DistinctProjection,
        RowCountProjection
    }

}
