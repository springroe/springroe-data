/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.springroe.data.mybatis.repository.query;

import com.samskivert.mustache.Mustache;
import org.apache.ibatis.session.Configuration;
import org.springframework.data.mybatis.dialect.Dialect;
import org.springframework.data.mybatis.dialect.internal.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.springframework.data.mybatis.dialect.internal.StandardDialectResolver;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.data.mybatis.mapping.MybatisPersistentEntity;
import org.springframework.data.mybatis.repository.query.DefaultCollector;
import org.springframework.data.mybatis.repository.query.EscapeCharacter;
import org.springframework.data.mybatis.repository.query.MybatisPrecompiler;
import org.springframework.data.repository.core.RepositoryInformation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Simple mybatis precompiler.
 *
 * @author JARVIS SONG
 */
class DefaultMybatisPrecompiler implements MybatisPrecompiler {

    static final String DEFAULT_SEQUENCE_NAME = "seq_spring_data_mybatis";

    protected final MybatisMappingContext mappingContext;

    protected final Configuration configuration;

    protected final String namespace;

    protected final MybatisPersistentEntity<?> persistentEntity;

    protected final Dialect dialect;

    protected Class<?> repositoryInterface;

    protected EscapeCharacter escape;

    private final Mustache.Compiler mustache;

    DefaultMybatisPrecompiler(MybatisMappingContext mappingContext, Configuration configuration,
                              RepositoryInformation repositoryInformation) {
        this(mappingContext, configuration, repositoryInformation.getDomainType());

        this.repositoryInterface = repositoryInformation.getRepositoryInterface();
    }

    DefaultMybatisPrecompiler(MybatisMappingContext mappingContext, Configuration configuration, Class<?> domainType) {
        this.mappingContext = mappingContext;
        this.configuration = configuration;
        this.namespace = domainType.getName();
        this.persistentEntity = mappingContext.getRequiredPersistentEntity(domainType);
        this.dialect = StandardDialectResolver.INSTANCE.resolveDialect(
                new DatabaseMetaDataDialectResolutionInfoAdapter(configuration.getEnvironment().getDataSource()));
        this.mustache = Mustache.compiler().withLoader(name -> {
            String path = "io/github/springroe/data/mybatis/repository/query/template/" + name + ".mustache";
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        }).escapeHTML(false).withCollector(new DefaultCollector());
    }

    @Override
    public void precompile() {
        //TODO 预编译mybatis执行脚本
    }

}
