package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class DistinctProjection extends AbstractProjection implements Projection {

    private Projection projection;

    DistinctProjection(Projection projection) {
        super(ProjectionType.DistinctProjection);
        this.projection = projection;
    }

    public Projection getProjection() {
        return projection;
    }
}
