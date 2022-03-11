package io.github.springroe.data.mybatis.domain;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.repository.Repository;
import io.github.springroe.data.mybatis.criterion.MybatisDataCriterion;

import java.io.Serializable;

/**
 * A super base of jpa type repository <br/>
 * mybatis repository base，继承结构查看See Also
 *
 * @param <T>  entity type
 * @param <ID> type of primary key
 * @author kongsiyu
 * @see DataCriterion
 * @see Repository
 */
public interface MybatisRepository<T extends Persistable<ID>, ID extends Serializable> extends
        Repository<T, ID>,
        org.springframework.data.mybatis.repository.MybatisRepository<T, ID>,
        MybatisDataCriterion<T, ID> {


    @Override
    default T getById(ID id) {
        return Repository.super.getById(id);
    }

    @Override
    default <S extends T> S update(S entity) {
        return Repository.super.update(entity);
    }
}
