package io.github.springroe.data.jpa.repository;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.repository.Repository;
import io.github.springroe.data.jpa.criterion.JpaDataCriterion;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * A super base of jpa type repository <br/>
 * jpa repository base，继承结构查看See Also
 *
 * @param <T>  entity type
 * @param <ID> type of primary key
 * @author kongsiyu
 * @see DataCriterion
 * @see Repository
 */
public interface JpaRepository<T extends Persistable<ID>, ID extends Serializable> extends
        Repository<T, ID>,
        org.springframework.data.jpa.repository.JpaRepository<T, ID>,
        JpaDataCriterion<T, ID> {

    @Override
    default T getById(ID id) {
        return Repository.super.getById(id);
    }

    @Override
    default EntityManager getEntityManager() {
        return (EntityManager) getSimpleJpaRepositoryFieldValue("em");
    }

    @Override
    default Class<T> getEntityClass() {
        JpaMetamodelEntityInformation<T, ID> info = (JpaMetamodelEntityInformation<T, ID>) getSimpleJpaRepositoryFieldValue("entityInformation");
        assert info != null;
        return info.getJavaType();
    }

    default SimpleJpaRepository<T, ID> getSimpleJpaRepository() {
        return (SimpleJpaRepository<T, ID>) AopProxyUtils.getSingletonTarget(this);
    }


    @Nullable
    default Object getSimpleJpaRepositoryFieldValue(String fieldName) {
        Field field = ReflectionUtils.findField(SimpleJpaRepository.class, fieldName);
        assert field != null;
        field.setAccessible(true);
        return ReflectionUtils.getField(field, getSimpleJpaRepository());

    }
}
