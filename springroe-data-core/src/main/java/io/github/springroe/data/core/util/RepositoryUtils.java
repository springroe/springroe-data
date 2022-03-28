package io.github.springroe.data.core.util;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class RepositoryUtils {

    public static Object getSpringProxyRepository(Object repositoryInterface) {
        return AopProxyUtils.getSingletonTarget(repositoryInterface);
    }


    @Nullable
    public static Object getSpringProxyRepositoryFieldValue(Object repositoryInterface, Class springProxyRepository, String fieldName) {
        Field field = ReflectionUtils.findField(springProxyRepository, fieldName);
        assert field != null;
        field.setAccessible(true);
        return ReflectionUtils.getField(field, getSpringProxyRepository(repositoryInterface));

    }
}
