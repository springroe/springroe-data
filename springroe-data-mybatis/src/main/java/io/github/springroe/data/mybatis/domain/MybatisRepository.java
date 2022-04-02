package io.github.springroe.data.mybatis.domain;

import io.github.springroe.data.core.criterion.DataCriterion;
import io.github.springroe.data.core.domain.Persistable;
import io.github.springroe.data.core.repository.Repository;
import io.github.springroe.data.core.util.RepositoryUtils;
import io.github.springroe.data.mybatis.criterion.MybatisDataCriterion;
import io.github.springroe.data.mybatis.domain.query.Conditions;
import io.github.springroe.data.mybatis.domain.query.JoinType;
import io.github.springroe.data.mybatis.domain.query.QueryCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mybatis.repository.support.MybatisEntityInformation;
import org.springframework.data.mybatis.repository.support.SimpleMybatisRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A super base of jpa type repository <br/>
 * mybatis repository base，继承结构查看See Also
 *
 * @param <T>  entity type
 * @param <ID> type of primary key
 * @author kongsiyu
 * @see DataCriterion
 * @see Repository
 */
public interface MybatisRepository<T extends Persistable<ID>, ID extends Serializable> extends
        Repository<T, ID>,
        org.springframework.data.mybatis.repository.MybatisRepository<T, ID>,
        MybatisDataCriterion<T, ID> {

    @Override
    default T getById(ID id) {
        return Repository.super.getById(id);
    }

    @Override
    default <S extends T> S update(S entity) {
        return Repository.super.update(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    default Class<T> getEntityClass() {
        MybatisEntityInformation<T, ID> info = (MybatisEntityInformation<T, ID>) getDefaultMybatisRepositoryFieldValue("entityInformation");
        assert info != null;
        return info.getJavaType();
    }

    @Nullable
    default Object getDefaultMybatisRepositoryFieldValue(String fieldName) {
        return RepositoryUtils.getSpringProxyRepositoryFieldValue(this, SimpleMybatisRepository.class, fieldName);
    }

    default T getOneByMap(Map<String, Object> map) {
        List<T> list = selectListByMap(map);
        if (list.size() > 1) throw new RuntimeException("More Than One Record Data Return");
        if (list.isEmpty()) return null;
        else return list.get(0);
    }

    List<T> selectListByMap(Map<String, Object> map);

    int deleteByMap(Map<String, Object> map);

    long countByMap(Map<String, Object> map);

    default boolean existByMap(Map<String, Object> map) {
        return countByMap(map) > 0;
    }

    List<T> listByQc(@NonNull QueryCondition queryCondition);

    Integer countByQc(@NonNull QueryCondition queryCondition);

    List<Map<String, Object>> findTuple(@NonNull QueryCondition queryCondition);

    List<T> pageList(@NonNull Integer offset, Integer limit, QueryCondition queryCondition);

    default Page<T> findByPage(@NonNull Integer pageIndex, @NonNull Integer pageSize, @NonNull QueryCondition queryCondition) {
        List<T> list = new ArrayList<>();
        queryCondition.distinct(true);
        Integer total = countByQc(queryCondition);
        int offset = pageIndex > 1 ? (pageIndex - 1) * pageSize : 0;
        if (total > 0) list = pageList(offset, pageSize, queryCondition);
        return PageableExecutionUtils.getPage(list, PageRequest.of(pageIndex, pageSize), () -> total);
    }

    default T uniqueByQc(@NonNull QueryCondition queryCondition) {
        List<T> list = listByQc(queryCondition);
        if (null != list && list.size() > 0) {
            if (list.size() > 1) {
                throw new RuntimeException("More Than One Record Data Return");
            }
            return list.get(0);
        }
        return null;
    }

    default List<T> findListByProperty(@NonNull String propertyName, @NonNull Object propertyValue) {
        QueryCondition qc = getQc();
        qc.add(Conditions.equal(propertyName, propertyValue));
        return listByQc(qc);
    }

    default T findByProperty(@NonNull String propertyName, @NonNull Object propertyValue) {
        QueryCondition qc = getQc();
        qc.add(Conditions.equal(propertyName, propertyValue));
        return uniqueByQc(qc);
    }

    default List<T> findByJoin(@NonNull JoinType joinType, @NonNull String... joinPath) {
        QueryCondition qc = getQc(getEntityClass().getSimpleName());
        for (int i = 0; i < joinPath.length; i++) {
            String join = joinPath[i];
            if (i > 0) {
                qc.createAlias(joinPath[i - 1] + "." + join, join, joinType);
            } else {
                qc.createAlias(join, join, joinType);
            }
        }
        return listByQc(qc);
    }

    default QueryCondition getQc(String alias) {
        return QueryCondition.forAlias(alias);
    }

    default QueryCondition getQc() {
        return QueryCondition.getInstance();
    }


}
