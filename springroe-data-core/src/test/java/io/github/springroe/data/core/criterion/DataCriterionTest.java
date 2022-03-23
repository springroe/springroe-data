package io.github.springroe.data.core.criterion;

import com.querydsl.core.types.OrderSpecifier;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.exception.DataException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Predicate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataCriterionTest {

    DataCriterion<TestEntity, String> testDataCriterion;


    @BeforeEach
    void setUp() {
        testDataCriterion = new TestDataCriterion();
    }

    @AfterEach
    void tearDown() {
        testDataCriterion = null;
    }

    @Test
    void optionalNotEmptyCheck() {
        TestEntity entity = new TestEntity();
        Optional<TestEntity> nullResult = Optional.empty();
        Optional<TestEntity> valueResult = Optional.of(entity);
        assertThrows(DataException.class, () -> {
            testDataCriterion.optionalNotEmptyCheck(nullResult);
        });
        assertEquals(entity, testDataCriterion.optionalNotEmptyCheck(valueResult));
    }

    @Test
    void firstNotEmptyCheck() {
        TestEntity entity = new TestEntity();
        Page<TestEntity> emptyPage = new PageImpl<>(new ArrayList<>());
        Page<TestEntity> valuePage = new PageImpl<>(new ArrayList<TestEntity>() {{
            add(entity);
        }});
        assertThrows(DataException.class, () -> {
            testDataCriterion.firstNotEmptyCheck(emptyPage);
        });
        assertEquals(entity, testDataCriterion.firstNotEmptyCheck(valuePage));
    }

    @Test
    void getEntityClass() {
        assertEquals(TestEntity.class, testDataCriterion.getEntityClass());
    }

    @Test
    void testOptionalNotEmptyCheck() {
    }

    @Test
    void testFirstNotEmptyCheck() {
    }

    @Setter
    @Getter
    public class TestEntity implements Persistable<String> {

        protected String id;
        @CreatedDate
        @Schema(description = "创建时间")
        protected Date createdDate;

        @LastModifiedDate
        @Schema(description = "最后修改时间")
        protected Date lastModifiedDate;

        @Schema(description = "逻辑删除标记")
        protected boolean deleted = false;

        @Schema(description = "不可编辑的")
        protected boolean immutable = false;
    }

    public class TestDataCriterion implements DataCriterion<TestEntity, String> {

        @Override
        public Class<TestEntity> getEntityClass() {
            return TestEntity.class;
        }

        @Override
        public <S extends TestEntity> S save(S entity) {
            return null;
        }

        @Override
        public <S extends TestEntity> Iterable<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<TestEntity> findById(String s) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(String s) {
            return false;
        }

        @Override
        public Iterable<TestEntity> findAll() {
            return null;
        }

        @Override
        public Iterable<TestEntity> findAllById(Iterable<String> strings) {
            return null;
        }

        @Override
        public Iterable<TestEntity> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<TestEntity> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(String s) {

        }

        @Override
        public void delete(TestEntity entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends String> strings) {

        }

        @Override
        public void deleteAll(Iterable<? extends TestEntity> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public Optional<TestEntity> findOne(com.querydsl.core.types.Predicate predicate) {
            return Optional.empty();
        }

        @Override
        public Iterable<TestEntity> findAll(com.querydsl.core.types.Predicate predicate) {
            return null;
        }

        @Override
        public Iterable<TestEntity> findAll(com.querydsl.core.types.Predicate predicate, Sort sort) {
            return null;
        }

        @Override
        public Iterable<TestEntity> findAll(com.querydsl.core.types.Predicate predicate, OrderSpecifier<?>... orders) {
            return null;
        }

        @Override
        public Iterable<TestEntity> findAll(OrderSpecifier<?>... orders) {
            return null;
        }

        @Override
        public Page<TestEntity> findAll(com.querydsl.core.types.Predicate predicate, Pageable pageable) {
            return null;
        }

        @Override
        public long count(com.querydsl.core.types.Predicate predicate) {
            return 0;
        }

        @Override
        public boolean exists(com.querydsl.core.types.Predicate predicate) {
            return false;
        }

        @Override
        public <S extends TestEntity, R> R findBy(com.querydsl.core.types.Predicate predicate, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public <S extends TestEntity> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends TestEntity> Iterable<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends TestEntity> Iterable<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends TestEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends TestEntity> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends TestEntity> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends TestEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }
    }
}