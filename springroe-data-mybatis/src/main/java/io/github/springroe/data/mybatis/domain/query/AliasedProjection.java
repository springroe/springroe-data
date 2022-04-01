package io.github.springroe.data.mybatis.domain.query;

import org.springframework.util.Assert;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class AliasedProjection extends AbstractProjection implements Projection {

    private Projection projection;

    private String alias;

    AliasedProjection(Projection projection, String alias) {
        super(ProjectionType.AliasedProjection);
        Assert.notNull(projection, "projection not allow null");
        Assert.notNull(alias, "alias not allow null");
        if (projection instanceof ProjectionList) {
            throw new IllegalArgumentException("AliasedProjection don't contain projection type is ProjectionList");
        }
        this.projection = projection;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public Projection getProjection() {
        return projection;
    }
}
