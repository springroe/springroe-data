package io.github.springroe.data.mybatis.criterion;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;

import java.io.Serializable;


public interface MybatisDataCriterion<T extends Persistable<ID>, ID extends Serializable>
        extends DataCriterion<T, ID> {

}
