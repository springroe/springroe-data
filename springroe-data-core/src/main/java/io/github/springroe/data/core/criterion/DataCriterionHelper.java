package io.github.springroe.data.core.criterion;

import io.github.springroe.data.core.exception.DataException;
import io.github.springroe.data.core.util.EntityUtils;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface DataCriterionHelper<T> {


    /**
     * @return entity class
     */
    Class<T> getEntityClass();


    /**
     * Optional not null valid logic
     *
     * @param result one size query result
     * @return not null result value
     */
    default T optionalNotEmptyCheck(@NonNull Optional<T> result) {
        if (!result.isPresent()) {
            String name = EntityUtils.getEntityComment(getEntityClass());
            throw new DataException(name + "不存在");
        }
        return result.get();
    }

    /**
     * Optional Collections not null valid logic
     *
     * @param result collection query result
     * @return not null result value
     */
    default T firstNotEmptyCheck(@NonNull Page<T> result) {
        if (result.isEmpty()) {
            String name = EntityUtils.getEntityComment(getEntityClass());
            throw new DataException(name + "不存在");
        }
        return optionalNotEmptyCheck(result.stream().findFirst());
    }
}
