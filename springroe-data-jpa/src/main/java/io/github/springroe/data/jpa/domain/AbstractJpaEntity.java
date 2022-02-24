package io.github.springroe.data.jpa.domain;

import io.github.springroe.data.core.domain.Persistable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
public abstract class AbstractJpaEntity<ID extends Serializable> implements Persistable<ID> {

    /**
     * 创建时间
     */
    @Column(length = 19, nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Schema(description = "创建时间")
    protected Date createdDate;

    /**
     * 最后修改时间
     */
    @Column(length = 19, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Schema(description = "最后修改时间")
    protected Date lastModifiedDate;

    /**
     * 逻辑删除标记
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    @Schema(description = "逻辑删除标记")
    protected boolean deleted = false;

    /**
     * 不可编辑的
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    @Schema(description = "不可编辑的")
    protected boolean immutable = false;

}