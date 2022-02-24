package io.github.springroe.data.core.domain;

import java.io.Serializable;

/**
 * entity sort support
 *
 * @param <ID> type of primary key
 * @author kongisyu
 */
public interface Sortable<ID extends Serializable> extends Persistable<ID> {
    /**
     * @return sortOrder
     */
    long getSortOrder();

    /**
     * @param sortOrder sortOrder
     */
    void setSortOrder(long sortOrder);
}
