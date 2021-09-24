package io.github.springroe.data.core.repository;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.entity.Entity;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

public interface Repository<T extends Entity<ID>, ID extends Serializable> extends PagingAndSortingRepository<T, ID>,
        QuerydslPredicateExecutor<T>,
        DataCriterion<T, ID>,
        CrudRepository<T, ID> {
}