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
package io.github.springroe.data.mybatis.repository.config;

import io.github.springroe.data.mybatis.repository.entity.PersistentEntityHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@link FactoryBean} to setup {@link MybatisMappingContext} instances from Spring
 * configuration.
 *
 * @author JARVIS SONG
 */
@Slf4j
class MybatisMappingContextFactoryBean extends AbstractFactoryBean<MybatisMappingContext>
		implements ApplicationContextAware {

	private final Map<Class<?>, Class<?>> mapping;

	private @Nullable ListableBeanFactory beanFactory;

	private Configuration configuration;

	MybatisMappingContextFactoryBean(Map<Class<?>, Class<?>> mapping, SqlSessionTemplate sqlSessionTemplate) {
		this.mapping = mapping;
		this.configuration = sqlSessionTemplate.getConfiguration();
	}

	@Override
	public Class<?> getObjectType() {
		return MybatisMappingContext.class;
	}

	@Override
	protected MybatisMappingContext createInstance() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Initializing MybatisMappingContext...");
		}
		Set<? extends Class<?>> initialEntitySet;
		if (null == this.mapping) {
			initialEntitySet = new HashSet<>();
		}
		else {
			initialEntitySet = new HashSet<>(this.mapping.values());
		}

		MultiValueMap<Class<?>, Class<?>> entityRepositoryMapping = new LinkedMultiValueMap<>();
		for (Map.Entry<Class<?>, Class<?>> entry : this.mapping.entrySet()) {
			Class<?> repository = entry.getKey();
			Class<?> entity = entry.getValue();
			entityRepositoryMapping.add(entity, repository);
		}

		MybatisMappingContext context = new MybatisMappingContext();
		if (this.configuration.isMapUnderscoreToCamelCase()) {
			context.setFieldNamingStrategy(new SnakeCaseFieldNamingStrategy());
		}

		context.setInitialEntitySet(initialEntitySet);
		context.setEntityRepositoryMapping(entityRepositoryMapping);
		context.initialize();
		if (log.isDebugEnabled()) {
			log.debug("Finished initializing MybatisMappingContext!");
		}
		PersistentEntityHelper.mappingContext = context;
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.beanFactory = applicationContext;
	}

}
