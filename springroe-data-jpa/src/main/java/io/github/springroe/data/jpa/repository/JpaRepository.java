package io.github.springroe.data.jpa.repository;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.repository.Repository;

import java.io.Serializable;

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
public interface JpaRepository<T extends Persistable<ID>, ID extends Serializable> extends Repository<T, ID> {
}
