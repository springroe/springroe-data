package io.github.springroe.data.core.domain;

import java.io.Serializable;
import java.util.Date;


/**
 * entity's super base
 * springroe-data顶层entity
 *
 * @param <ID> type of primary key
 * @author kongsiyu
 */
public interface Persistable<ID extends Serializable> extends org.springframework.data.domain.Persistable<ID> {

    /**
     * @param id id
     */
    void setId(ID id);

    /**
     * @return 创建时间
     */
    Date getCreatedDate();

    /**
     * 设置创建时间
     *
     * @param creationDate 创建时间
     */
    void setCreatedDate(Date creationDate);

    /**
     * 最后修改时间
     *
     * @return void
     */
    Date getLastModifiedDate();

    /**
     * 设置最后修改时间
     *
     * @param lastModifiedDate 最后修改时间
     */
    void setLastModifiedDate(Date lastModifiedDate);

    /**
     * 是否已删除
     *
     * @return 删除标记
     */
    boolean isDeleted();

    /**
     * 设置删除标记
     *
     * @param deleted 删除标记
     */
    void setDeleted(boolean deleted);


    /**
     * if false,this data cannot be edited(all super api) <br/>
     * 是否不可编辑数据 <br/>
     * 通常用于系统数据系统数据通常不能通过通用接口删除和修改 <br/>
     *
     * @return immutable
     */
    boolean isImmutable();


    /**
     * @param immutable immutable
     */
    void setImmutable(boolean immutable);

    @Override
    default boolean isNew() {
        return getId() == null || getId().toString().isEmpty();
    }
}