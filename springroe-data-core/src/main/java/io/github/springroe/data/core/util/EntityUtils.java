package io.github.springroe.data.core.util;

import io.github.springroe.data.core.entity.Entity;
import org.springframework.util.ClassUtils;

public class EntityUtils {


    static final boolean SWAGGER_PRESENT = ClassUtils.isPresent("io.swagger.annotations.ApiModel", Entity.class.getClassLoader());
    static final boolean SWAGGER_V3_PRESENT = ClassUtils.isPresent("io.swagger.v3.oas.annotations.media.Schema", Entity.class.getClassLoader());

    private EntityUtils() {
    }

    /**
     * 根据entity class 获取 entity 注释 <br>
     * 主要根据swagger annotations 获取，同时兼容swagger旧版和sagger v3
     *
     * @param entityClass entity class
     * @return entity 注释
     */
    public static String getEntityComment(Class<?> entityClass) {
        String name = null;

        if (SWAGGER_PRESENT) {
            io.swagger.annotations.ApiModel apiModel = entityClass.getAnnotation(io.swagger.annotations.ApiModel.class);
            if (apiModel != null) name = apiModel.value();
        }
        if (name == null && SWAGGER_V3_PRESENT) {
            io.swagger.v3.oas.annotations.media.Schema schema = entityClass.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
            if (schema != null) name = schema.name();
        }
        if (name == null) {
            name = entityClass.getSimpleName();
        }
        return name;
    }

}
