package io.github.springroe.jpa.example.repository.entity;

import io.github.springroe.data.jpa.domain.AbstractJpaStringEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Example extends AbstractJpaStringEntity {

    @Column(name = "name")
    public String name;

}
