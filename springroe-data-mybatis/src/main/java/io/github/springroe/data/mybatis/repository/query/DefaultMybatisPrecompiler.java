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

import io.github.springroe.data.mybatis.repository.entity.PersistentEntityHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.data.mybatis.mapping.MybatisPersistentEntity;
import org.springframework.data.mybatis.mapping.MybatisPersistentProperty;
import org.springframework.data.repository.core.RepositoryInformation;

import java.util.*;

/**
 * Simple mybatis precompiler.
 *
 * @author JARVIS SONG
 */
class DefaultMybatisPrecompiler extends AbstractMybatisPrecompiler {

    DefaultMybatisPrecompiler(MybatisMappingContext mappingContext, Configuration configuration, RepositoryInformation repositoryInformation) {
        super(mappingContext, configuration, repositoryInformation);
    }

    @Override
    protected String doPrecompile() {
        createResultMap();
        StringBuilder sb = new StringBuilder();
        sb.append(addCountByQc());
        sb.append(addListByQc());
        sb.append(addCreatePageListByQc());
        sb.append(addFindTuple());
        return sb.toString();
    }

    private String addCountByQc() {
        return "";
    }

    private String addListByQc() {
        return "";
    }

    private String addCreatePageListByQc() {
        return "";
    }

    private String addFindTuple() {
        return "";
    }

    @Override
    protected List<MappedStatement> doPrecompileMappedStatements() {
        return Collections.emptyList();
    }

    private void createResultMap() {
        //TODO 创建resultMap
    }

    private static ResultMap getResultMap(Class<?> rootEntityClass,
                                          Class<?> prevEntityClass,
                                          Class<?> currentEntityClass,
                                          String propertyPath,
                                          Configuration configuration,
                                          boolean nested) {
        List<ResultMapping> resultMappings = new ArrayList<>();
        MybatisPersistentEntity<?> persistentEntity = PersistentEntityHelper.persistentEntity(currentEntityClass);
        MybatisPersistentProperty idProperty = persistentEntity.getIdProperty();
        String keyProperty = idProperty.getName();
        Class<?> keyType = PersistentEntityHelper.getPropertyType(idProperty);
        String keyPropertyPath = keyProperty;
        if (StringUtils.isNotBlank(propertyPath)) {
            keyPropertyPath = propertyPath + "." + keyPropertyPath;
        }
        ResultMapping.Builder idResultMappingBuilder = new ResultMapping
                .Builder(configuration, keyProperty, PersistentEntityHelper.getPropertyColumnAlias(rootEntityClass, keyPropertyPath.split("\\.")), keyType);
        idResultMappingBuilder.flags(Arrays.asList(ResultFlag.ID));
        resultMappings.add(idResultMappingBuilder.build());
        persistentEntity.doWithProperties((PropertyHandler<MybatisPersistentProperty>) persistentProperty -> {
            ResultMapping.Builder resultMappingBuilder = null;
            String property = persistentProperty.getName();
            String fullPropertyPath = property;
            Class<?> propertyType = PersistentEntityHelper.getPropertyType(persistentProperty);
            if (StringUtils.isNotBlank(propertyPath)) {
                fullPropertyPath = propertyPath + "." + property;
            }
            String column = persistentProperty.getColumn().getName().getText();
            if (StringUtils.isNotBlank(column)) {
                column = PersistentEntityHelper.getPropertyColumnAlias(rootEntityClass, fullPropertyPath.split("\\."));
            }
            if (persistentProperty.isAssociation()) {
                if (prevEntityClass != propertyType) {
                    PersistentEntityHelper.AssociationType associationType = PersistentEntityHelper.associationType(persistentProperty);
                    Class<?> javaType = propertyType;
                    if (associationType == PersistentEntityHelper.AssociationType.OneToMany
                            || associationType == PersistentEntityHelper.AssociationType.ManyToMany) {
                        javaType = List.class;
                    }
                    ResultMap resultMap = getResultMap(rootEntityClass, currentEntityClass, propertyType, fullPropertyPath, configuration, true);
                    resultMappingBuilder = new ResultMapping
                            .Builder(configuration, property, column, javaType);
                    configuration.addResultMap(resultMap);
                    resultMappingBuilder.nestedResultMapId(resultMap.getId());
                }
            } else {
                resultMappingBuilder = new ResultMapping
                        .Builder(configuration, property, column, propertyType);
                Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) persistentProperty.getColumn().getTypeHandler();
                if (null != typeHandler) {
                    try {
                        resultMappingBuilder.typeHandler(typeHandler.newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (null != resultMappingBuilder) {
                resultMappings.add(resultMappingBuilder.build());
            }
        });
        String resultMapId = "BaseMapperResultMap";
        if (nested) {
            resultMapId = rootEntityClass.getSimpleName() + "." + propertyPath + "[" + UUID.randomUUID() + "-nested" + "]";
        }
        ResultMap.Builder builder = new ResultMap.Builder(configuration, resultMapId, currentEntityClass, resultMappings);
        return builder.build();
    }

}
