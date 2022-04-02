/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.springroe.data.mybatis.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.exception.DataException;
import io.github.springroe.data.mybatis.domain.MybatisRepository;
import io.github.springroe.data.mybatis.domain.query.Conditions;
import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mybatis.repository.support.MybatisEntityInformation;
import org.springframework.data.mybatis.repository.support.SimpleMybatisRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The simple implementation of {@link MybatisRepository}.
 *
 * @param <T>  domain's type
 * @param <ID> domain primary key's type
 * @author JARVIS SONG
 * @since 1.0.0
 */
@Repository
@Transactional(readOnly = true)
public class DefaultMybatisRepository<T extends Persistable<ID>, ID extends Serializable> extends SimpleMybatisRepository<T, ID> implements MybatisRepository<T, ID> {

    public DefaultMybatisRepository(MybatisEntityInformation<T, ID> entityInformation,
                                    RepositoryInformation repositoryInformation, SqlSessionTemplate sqlSessionTemplate) {
        super(entityInformation, repositoryInformation, sqlSessionTemplate);
    }

    @Override
    public List<T> selectListByMap(Map<String, Object> map) {
        QueryCondition qc = getQc();
        map.forEach((k, v) -> qc.add(Conditions.equal(k, v)));
        return listByQc(qc);
    }

    @Override
    public int deleteByMap(Map<String, Object> map) {
        return 0;
    }

    @Override
    public long countByMap(Map<String, Object> map) {
        QueryCondition qc = getQc();
        map.forEach((k, v) -> qc.add(Conditions.equal(k, v)));
        return countByQc(qc);
    }

    @Override
    public List<T> listByQc(QueryCondition queryCondition) {
        return null;
    }

    @Override
    public Integer countByQc(QueryCondition queryCondition) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findTuple(QueryCondition queryCondition) {
        return null;
    }

    @Override
    public List<T> pageList(Integer offset, Integer limit, QueryCondition queryCondition) {
        return null;
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        Assert.notNull(ids, "The given iterable of ids must not be null.");
        List<ID> list = StreamSupport.stream(ids.spliterator(), false).map((id) -> (ID) id).collect(Collectors.toList());
        super.deleteInBatchById(list);
    }

    @Override
    public Optional<T> findOne(Predicate predicate) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public Iterable<T> findAll(Predicate predicate) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public Iterable<T> findAll(OrderSpecifier<?>... orders) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public long count(Predicate predicate) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public boolean exists(Predicate predicate) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public <S extends T, R> R findBy(Predicate predicate, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new DataException("An operation is not implemented.");
    }

    @Override
    public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new DataException("An operation is not implemented.");
    }
}
