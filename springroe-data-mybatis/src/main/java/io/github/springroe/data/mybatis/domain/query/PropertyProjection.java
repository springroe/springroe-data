package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class PropertyProjection extends SimpleProjection {

    PropertyProjection(String propertyName) {
        super(propertyName, AbstractProjection.ProjectionType.PropertyProjection);
    }

}
