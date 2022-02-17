package io.github.springroe.data.jpa.util;

import io.github.springroe.data.core.entity.Entity;
import io.github.springroe.data.jpa.domain.OneToManyAttributeData;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

public class JpaUtils {


    /**
     *
     * @param entityManager
     * @param query
     * @param root
     * @param joins
     * @return
     */
    public static List<Path<?>> getBasicSelections(EntityManager entityManager, CriteriaQuery<?> query, From<?, ?> root, String[] joins) {
        return null;
    }


    /**
     *
     * @param jpaUtils
     * @param entityManager
     * @param criteriaQuery
     * @param from
     * @param arrstring
     * @param n
     * @param object
     * @return
     */
    public static List<?> getBasicSelections$default(JpaUtils jpaUtils, EntityManager entityManager, CriteriaQuery<?> criteriaQuery, From<?, ?> from, String[] arrstring, int n, Object object) {
        if ((n & 8) != 0) {
            arrstring = new String[]{};
        }
        return getBasicSelections(entityManager, criteriaQuery, from, arrstring);
    }

    /**
     *
     * @param entityClass
     * @param tuple
     * @param selections
     * @param baseSelections
     * @param toOneJoinMap
     * @param oneToManyDataList
     * @param <T>
     * @return
     */
    public final <T extends Entity<?>> T buildEntity(Class<T> entityClass, Tuple tuple, List<? extends Selection<?>> selections, List<? extends Selection<?>> baseSelections, Map<Join<?, ?>, ? extends List<? extends Path<?>>> toOneJoinMap, List<OneToManyAttributeData> oneToManyDataList) {
        return null;
    }


}
