package io.github.springroe.data.redis.criterion;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;

import java.io.Serializable;


public interface RedisDataCriterion<T extends Persistable<ID>, ID extends Serializable>
        extends DataCriterion<T, ID> {

}
