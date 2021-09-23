package com.springroe.data.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.Date;

@Schema
public interface Entity<ID extends Serializable> extends Serializable {

    ID getId();

    void setId(ID id);

    /**
     * 创建时间
     */
    Date getCreateTime();

    void setCreateTime(Date createTime);

    /**
     * 最后修改时间
     */
    Date getLastModifiedTime();

    void getLastModifiedTime(Date lastModifiedTime);


    boolean swaggerPresent = ClassUtils.isPresent("io.swagger.annotations.ApiModel", Entity.class.getClassLoader());
    boolean swaggerV3Present = ClassUtils.isPresent("io.swagger.v3.oas.annotations.media.Schema", Entity.class.getClassLoader());

}
