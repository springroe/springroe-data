package io.github.springroe.data.mongo.domain;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.repository.Repository;
import io.github.springroe.data.core.util.RepositoryUtils;
import io.github.springroe.data.mongo.criterion.MongoDataCriterion;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;

/**
 * A super base of jpa type repository <br/>
 * mongo repository base，继承结构查看See Also
 *
 * @param <T>  entity type
 * @param <ID> type of primary key
 * @author kongsiyu
 * @see DataCriterion
 * @see Repository
 */
public interface MongoRepository<T extends Persistable<ID>, ID extends Serializable> extends Repository<T, ID>, org.springframework.data.mongodb.repository.MongoRepository<T, ID>, MongoDataCriterion<T, ID> {


    @Override
    default MongoOperations getOperations() {
        return (MongoOperations) RepositoryUtils.getSpringProxyRepositoryFieldValue(this, SimpleMongoRepository.class, "mongoOperations");
    }

    @Override
    default MongoTemplate getTemplate() {
        return (MongoTemplate) getOperations();
    }
}
