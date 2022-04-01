package io.github.springroe.data.mybatis.domain.query;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class ProjectionList extends AbstractProjection implements Projection {

    private List<Projection> projections = new ArrayList();

    ProjectionList() {
        super(ProjectionType.CompoundProjection);
    }

    public ProjectionList addProjection(Projection projection) {
        Assert.notNull(projection, "projection not allow null");
        if (projection instanceof ProjectionList) {
            this.projections.addAll(((ProjectionList) projection).getProjections());
        } else {
            this.projections.add(projection);
        }
        return this;
    }

    public List<Projection> getProjections() {
        return projections;
    }

}
