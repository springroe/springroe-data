package io.github.springroe.data.core.criterion;

import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.exception.DataException;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.io.Serializable;

/**
 * A top-level criterion for entity operators <br/>
 * entity对象操作的顶层标准，大致结构如下：<br/>
 * 　　　　　　　　DataCriterion<br/>
 * 　　　　　　　　/　　　　　　\<br/>
 * 　　　　ServiceBase　　　　RepositoryBase<br/>
 * 　　　　　　　　　　　　　　　/　　　　　\<br/>
 * 　　　　　　　　　　　　JpaImplement 　MongoImplement...
 *
 * @param <T>  entity type
 * @param <ID> type of primary key
 * @author kongsiyu
 */
public interface DataCriterion<T extends Persistable<ID>, ID extends Serializable> extends SpringDataCriterion<T, ID>,
        DataCriterionHelper<T>,
        QuerydslPredicateExecutor<T>,
        QueryByExampleExecutor<T>,
        QuerydslExtensions<T> {


    /**
     * find by id asset not null
     *
     * @param id ID
     * @return not null result
     */
    default T getById(ID id) {
        return optionalNotEmptyCheck(findById(id));
    }


    /**
     * add a new entity, if entity existed throw exception
     *
     * @param entity entity instance
     * @param <S>    entity class subtype include self.
     * @return entity instance self
     */
    default <S extends T> S add(S entity) {
        return save(entity);
    }

    /**
     * update a given entity.
     *
     * @param entity must not be null.
     * @return the saved entity; will never be null.
     * @throws IllegalArgumentException in case the given entity is null.
     */
    default <S extends T> S update(S entity) {
        if (entity.getId() == null) throw new DataException("id can not be empty!");
        return save(entity);
    }

}
