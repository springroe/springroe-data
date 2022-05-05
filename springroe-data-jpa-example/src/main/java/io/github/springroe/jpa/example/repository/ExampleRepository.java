package io.github.springroe.jpa.example.repository;

import io.github.springroe.data.jpa.repository.JpaRepository;
import io.github.springroe.jpa.example.repository.entity.Example;

public interface ExampleRepository extends JpaRepository<Example, String> {

}
