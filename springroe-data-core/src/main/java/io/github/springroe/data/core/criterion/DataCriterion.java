package io.github.springroe.data.core.criterion;

import io.github.springroe.data.core.entity.Entity;
import io.github.springroe.data.core.exception.DataException;
import io.github.springroe.data.core.util.EntityUtils;
import org.springframework.cglib.core.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.io.Serializable;
import java.util.Optional;

public interface DataCriterion<T extends Entity<ID>, ID extends Serializable> extends QuerydslPredicateExecutor<T>, QueryByExampleExecutor<T> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    <S extends T> S save(S entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return the saved entities; will never be {@literal null}. The returned {@literal Iterable} will have the same size
     * as the {@literal Iterable} passed as an argument.
     * @throws IllegalArgumentException in case the given {@link Iterable entities} or one of its entities is
     *                                  {@literal null}.
     */
    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    Optional<T> findById(ID id);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    boolean existsById(ID id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * Returns all instances of the type {@code T} with the given IDs.
     * <p>
     * If some or all ids are not found, no entities are returned for these IDs.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null} values.
     * @return guaranteed to be not {@literal null}. The size can be equal or less than the number of given
     * {@literal ids}.
     * @throws IllegalArgumentException in case the given {@link Iterable ids} or one of its items is {@literal null}.
     */
    Iterable<T> findAllById(Iterable<ID> ids);


    /**
     * Returns all entities sorted by the given options.
     *
     * @param sort
     * @return all entities sorted by the given options
     */
    Iterable<T> findAll(Sort sort);

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities.
     */
    long count();

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    void deleteById(ID id);

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes all instances of the type {@code T} with the given IDs.
     *
     * @param ids must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one of its elements is {@literal null}.
     * @since 2.5
     */
    void deleteAllById(Iterable<? extends ID> ids);

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
     */
    void deleteAll(Iterable<? extends T> entities);

    /**
     * Deletes all entities managed by the repository.
     */
    void deleteAll();


    /**
     * @return entity class
     */
    Class<T> getEntityClass();


    default T getById(ID id) {
        return optionalNotEmptyCheck(findById(id));
    }


    default T findOneNotEmpty(Predicate predicate) {
        return optionalNotEmptyCheck(findOne(predicate));
    }


    default Optional<T> findFirst(Predicate predicate) {
        return findAll(predicate, PageRequest.of(0, 1)).stream().findFirst();
    }

    default T findFirstNotEmpty(Predicate predicate) {
        return firstNotEmptyCheck(findAll(predicate, PageRequest.of(0, 1)));
    }

    default T optionalNotEmptyCheck(Optional<T> result) {

        if (!result.isPresent()) {
            String name = EntityUtils.getEntityComment(getEntityClass());
            throw new DataException(name + "不存在");
        }
        return result.get();
    }

    default T firstNotEmptyCheck(Page<T> result) {
        if (result.isEmpty()) {
            String name = EntityUtils.getEntityComment(getEntityClass());
            throw new DataException(name + "不存在");
        }
        return optionalNotEmptyCheck(result.stream().findFirst());
    }

    Optional<T> findOne(Predicate predicate);

    Page<T> findAll(Predicate predicate, Pageable pageable);

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
