package io.github.springroe.data.redis.domain;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.repository.Repository;
import io.github.springroe.data.redis.criterion.RedisDataCriterion;

import java.io.Serializable;

/**
 * A super base of jpa type repository <br/>
 * redis repository base，继承结构查看See Also
 *
 * @param <T>  entity type
 * @param <ID> type of primary key
 * @author kongsiyu
 * @see DataCriterion
 * @see Repository
 */
public interface RedisRepository<T extends Persistable<ID>, ID extends Serializable> extends
        Repository<T, ID>,
        RedisDataCriterion<T, ID> {


}
