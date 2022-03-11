package io.github.springroe.data.jpa.criterion;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public interface JpaDataCriterion<T extends Persistable<ID>, ID extends Serializable>
        extends DataCriterion<T, ID>,
        JpaSpecificationExecutor<T>, PathHelper {

    /**
     * @return @see javax.persistence.EntityManager
     */
    EntityManager getEntityManager();

    /**
     *
     * @return @see org.hibernate.SessionFactory
     */
    default SessionFactory getSessionFactory(){
        return getEntityManager().unwrap(SessionFactory.class);
    }

    /**
     * 以属性名称和属性值按equal的方式进行匹配<br/>
     * find list result match by entity property use equal
     *
     * @param property entity property name
     * @param value    property value
     * @return result iterable
     */
    default Iterable<T> findAllByProperty(String property, Object value) {
        return findAll((root, query, criteriaBuilder) -> criteriaBuilder.equal(getPath(root, property), value));
    }

    /**
     * 以属性名称和属性值按in的方式进行匹配(collection type)<br/>
     * find list result match by entity property use in collection values
     *
     * @param property entity property name
     * @param value    property collection values
     * @return result iterable
     */
    default Iterable<T> findAllInProperty(String property, Collection<Object> value) {
        if (value == null || value.isEmpty()) return new ArrayList<>(0);
        return findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = value.stream().map(r -> criteriaBuilder.equal(getPath(root, property), r)).collect(Collectors.toList());
            return criteriaBuilder.or(list.toArray(new Predicate[]{}));
        });
    }

    /**
     * 以属性名称和属性值按in的方式进行匹配(array type)<br/>
     * find list result match by entity property use in collection values
     *
     * @param property property entity property name
     * @param value    property array values
     * @return result iterable
     */
    default Iterable<T> findAllInProperty(String property, Object[] value) {
        return findAllInProperty(property, Arrays.asList(value));
    }

    /**
     * 以属性名称和属性值按in的方式进行匹配(string type[以','分割])<br/>
     * find list result match by entity property use in string collection values(split use ',')
     *
     * @param property property entity property name
     * @param value    property string collection values
     * @return result iterable
     */
    default Iterable<T> findAllInProperty(String property, @Nullable String value) {
        if (value == null || value.isEmpty()) return new ArrayList<>(0);
        return findAllInProperty(property, value.split(","));
    }

    /**
     * 获取唯一结果：以属性名称和属性值按equal的方式进行匹配<br/>
     * find unique result match by entity property use equal
     *
     * @param property entity property name
     * @param value    property value
     * @return result iterable
     * @throws RuntimeException throw exception if result length > 1
     */
    @Nullable
    default T getByProperty(String property, Object value) {
        return findOne((root, query, criteriaBuilder) -> criteriaBuilder.equal(getPath(root, property), value)).orElse(null);
    }

    /**
     * 获取第一个结果：以属性名称和属性值按equal的方式进行匹配<br/>
     * find first result match by entity property use equal
     *
     * @param property entity property name
     * @param value    property value
     * @return result iterable
     */
    @Nullable
    default T findFirst(String property, Object value) {
        return findFirst((root, query, criteriaBuilder) -> criteriaBuilder.equal(getPath(root, property), value)).orElse(null);
    }


    default Optional<T> findFirst(Specification<T> spec) {
        return findAll(spec, PageRequest.of(0, 1)).getContent().stream().findFirst();
    }

    default T findFirstNotEmpty(Specification<T> spec) {
        return firstNotEmptyCheck(findAll(spec, PageRequest.of(0, 1)));
    }

    /**
     * 获取唯一结果：按spring data jpa标准进行匹配<br/>
     * find unique result use spring data jpa conditions
     *
     * @param spec spring data jpa conditions
     * @return unique result or null result
     * @see org.springframework.data.jpa.domain.Specification
     */
    Optional<T> findOne(@Nullable Specification<T> spec);


    /**
     * @param spec
     * @return
     */
    default T findOneNotEmpty(Specification<T> spec) {
        return optionalNotEmptyCheck(findOne(spec));
    }

    /**
     * 获取列表结果：按spring data jpa标准进行匹配<br/>
     * find list result use spring data jpa conditions
     *
     * @param spec  spring data jpa conditions
     * @param joins join properties
     * @param sort  spring data sort
     * @return list result
     */
    List<T> findAll(Specification<T> spec, String[] joins, Sort sort);

    /**
     * 获取分页结果：按spring data jpa标准进行匹配<br/>
     * find page result use spring data jpa conditions
     *
     * @param spec     spring data jpa conditions
     * @param pageable spring data page
     * @param joins    join properties
     * @return page result
     */
    Page<T> findAll(Specification<T> spec, Pageable pageable, String[] joins);

}
