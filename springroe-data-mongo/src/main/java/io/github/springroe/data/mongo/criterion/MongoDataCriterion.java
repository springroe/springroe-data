package io.github.springroe.data.mongo.criterion;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import org.springframework.data.mongodb.core.MongoOperations;

import java.io.Serializable;


public interface MongoDataCriterion<T extends Persistable<ID>, ID extends Serializable>
        extends DataCriterion<T, ID> {

    MongoOperations getMongoOperations();
}
