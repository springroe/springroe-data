package io.github.springroe.data.core.repository;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * A super base of repository <br/>
 * repository base，继承结构查看See Also
 *
 * @param <T>  entity type
 * @param <ID> type of primary key
 * @author kongsiyu
 * @see DataCriterion
 */
public interface Repository<T extends Persistable<ID>, ID extends Serializable> extends PagingAndSortingRepository<T, ID>,
        DataCriterion<T, ID>,
        CrudRepository<T, ID> {
}