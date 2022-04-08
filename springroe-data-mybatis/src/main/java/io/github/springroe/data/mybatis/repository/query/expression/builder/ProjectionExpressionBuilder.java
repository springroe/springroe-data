package io.github.springroe.data.mybatis.repository.query.expression.builder;

import io.github.springroe.data.mybatis.domain.query.*;
import io.github.springroe.data.mybatis.repository.query.expression.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class ProjectionExpressionBuilder {

    private final static String NOT_NULL = "%s not allow null";

    private Projection projection;

    public ProjectionExpressionBuilder(Projection projection) {
        Assert.notNull(projection, String.format(NOT_NULL, "projection"));
        this.projection = projection;
    }

    public QueryConditionExpression build() {
        return translator(projection);
    }

    private ProjectionExpression translator(Projection projection) {
        AbstractProjection.ProjectionType projectionType = projection.getProjectionType();
        ProjectionExpression projectionExpression = null;
        switch (projectionType) {
            case PropertyProjection:
                projectionExpression = new PropertyProjectionExpression((PropertyProjection) projection);
                break;
            case DistinctProjection:
                DistinctProjection distinctProjection = (DistinctProjection) projection;
                projectionExpression = new DistinctProjectionExpression(translator(distinctProjection.getProjection()));
                break;
            case RowCountProjection:
                projectionExpression = new RowCountProjectionExpression();
                break;
            case AggregateProjection:
                projectionExpression = new AggregateProjectionExpression((AggregateProjection) projection);
                break;
            case AliasedProjection:
                AliasedProjection aliasedProjection = (AliasedProjection) projection;
                projectionExpression = new AliasedProjectionExpression(translator(aliasedProjection.getProjection()), aliasedProjection.getAlias());
                break;
            case CompoundProjection:
                List<ProjectionExpression> conditionExpressions = new ArrayList<>();
                ProjectionList projectionList = (ProjectionList) projection;
                List<Projection> projections = projectionList.getProjections();
                for (Projection p : projections) {
                    conditionExpressions.add(translator(p));
                }
                projectionExpression = new CompoundProjectionExpression(conditionExpressions);
                break;
        }
        return projectionExpression;
    }
}
