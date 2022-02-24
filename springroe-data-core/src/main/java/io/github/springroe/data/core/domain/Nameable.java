package io.github.springroe.data.core.domain;

import java.io.Serializable;

/**
 * An entity with a name
 * 有name属性的entity
 *
 * @param <ID> type of primary key
 */
public interface Nameable<ID extends Serializable> extends Persistable<ID> {

    String getName();

    void setName(String name);
}
