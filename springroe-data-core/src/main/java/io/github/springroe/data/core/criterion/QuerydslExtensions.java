package io.github.springroe.data.core.criterion;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface QuerydslExtensions<T> extends QuerydslPredicateExecutor<T>, DataCriterionHelper<T> {


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


}
