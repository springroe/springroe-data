package io.github.springroe.data.core.criterion;

import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.exception.DataException;
import io.github.springroe.data.core.util.EntityUtils;
import lombok.NonNull;
import org.springframework.cglib.core.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.io.Serializable;
import java.util.Optional;

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
public interface DataCriterion<T extends Persistable<ID>, ID extends Serializable> extends SpringDataCriterion<T, ID>, QuerydslPredicateExecutor<T>, QueryByExampleExecutor<T>, QuerydslExtensions<T> {


    /**
     * @return entity class
     */
    Class<T> getEntityClass();


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
     * find one result use predicate if result size > 0 throw exception
     *
     * @param predicate Predicate
     * @return result
     */
    Optional<T> findOne(Predicate predicate);


    /**
     * find one use Predicate asset not null
     *
     * @param predicate Predicate
     * @return not null result
     */
    default T findOneNotEmpty(Predicate predicate) {
        return optionalNotEmptyCheck(findOne(predicate));
    }


    /**
     * find one result use predicate if result size > 0 take first one
     *
     * @param predicate Predicate
     * @return result
     */
    default Optional<T> findFirst(Predicate predicate) {
        return findAll(predicate, PageRequest.of(0, 1)).stream().findFirst();
    }

    /**
     * find one result use predicate asset not null
     *
     * @param predicate Predicate
     * @return not null result
     */
    default T findFirstNotEmpty(Predicate predicate) {
        return firstNotEmptyCheck(findAll(predicate, PageRequest.of(0, 1)));
    }


    /**
     * find page use  Predicate
     *
     * @param predicate Predicate
     * @param pageable  jpa page param
     * @return page result
     */
    Page<T> findAll(Predicate predicate, Pageable pageable);

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


    /**
     * Optional not null valid logic
     *
     * @param result one size query result
     * @return not null result value
     */
    default T optionalNotEmptyCheck(@NonNull Optional<T> result) {
        if (!result.isPresent()) {
            String name = EntityUtils.getEntityComment(getEntityClass());
            throw new DataException(name + "不存在");
        }
        return result.get();
    }

    /**
     * Optional Collections not null valid logic
     *
     * @param result collection query result
     * @return not null result value
     */
    default T firstNotEmptyCheck(@NonNull Page<T> result) {
        if (result.isEmpty()) {
            String name = EntityUtils.getEntityComment(getEntityClass());
            throw new DataException(name + "不存在");
        }
        return optionalNotEmptyCheck(result.stream().findFirst());
    }

}
