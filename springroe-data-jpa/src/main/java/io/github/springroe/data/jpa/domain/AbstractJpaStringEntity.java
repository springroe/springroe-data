package io.github.springroe.data.jpa.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@Setter
@Getter
public abstract class AbstractJpaStringEntity extends AbstractJpaEntity<String> {

    /**
     * uuid
     */
    @Id
    @Column(updatable = false, unique = true, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Schema(description = "ID")
    protected String id;
}