package io.github.springroe.data.mybatis.domain.query;

import org.springframework.util.Assert;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class SimpleProjection extends AbstractProjection {

    private String propertyName;

    public SimpleProjection(String propertyName, ProjectionType projectionType) {
        super(projectionType);
        Assert.notNull(propertyName, "propertyName not allow null");
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Projection as(String alias) {
        return Projections.alias(this, alias);
    }

}
