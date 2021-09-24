package io.github.springroe.data.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

@Schema
public interface Entity<ID extends Serializable> extends Serializable {

    ID getId();

    void setId(ID id);

    /**
     * @return 创建时间
     */
    Date getCreateTime();

    /**
     * @param createTime 创建时间
     */
    void setCreateTime(Date createTime);

    /**
     * 最后修改时间
     * @return void
     */
    Date getLastModifiedTime();

    void getLastModifiedTime(Date lastModifiedTime);


}
