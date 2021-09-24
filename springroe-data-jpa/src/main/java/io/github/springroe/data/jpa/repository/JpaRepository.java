package io.github.springroe.data.jpa.repository;

import io.github.springroe.data.core.entity.Entity;
import io.github.springroe.data.core.repository.Repository;

import java.io.Serializable;

public interface JpaRepository<T extends Entity<ID>, ID extends Serializable> extends Repository<T, ID> {
}
