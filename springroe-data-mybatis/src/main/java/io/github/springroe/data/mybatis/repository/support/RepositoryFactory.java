package io.github.springroe.data.mybatis.repository.support;

import io.github.springroe.data.mybatis.repository.DefaultMybatisRepository;
import io.github.springroe.data.mybatis.repository.query.RepositoryPrepareProcessor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.data.mybatis.repository.query.EscapeCharacter;
import org.springframework.data.mybatis.repository.query.MybatisQueryLookupStrategy;
import org.springframework.data.mybatis.repository.query.MybatisQueryPrepareProcessor;
import org.springframework.data.mybatis.repository.query.MybatisRepositoryPrepareProcessor;
import org.springframework.data.mybatis.repository.support.MybatisEntityInformation;
import org.springframework.data.mybatis.repository.support.MybatisEntityInformationSupport;
import org.springframework.data.mybatis.repository.support.MybatisRepositoryImplementation;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.SurroundingTransactionDetectorMethodInterceptor;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryFactory extends RepositoryFactorySupport {

    private final MybatisMappingContext mappingContext;

    private final SqlSessionTemplate sqlSessionTemplate;

    private EntityPathResolver entityPathResolver;

    private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;

    public RepositoryFactory(MybatisMappingContext mappingContext, SqlSessionTemplate sqlSessionTemplate) {
        this.mappingContext = mappingContext;
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.entityPathResolver = SimpleEntityPathResolver.INSTANCE;

        this.addRepositoryProxyPostProcessor((factory, repositoryInformation) -> {
            if (hasMethodReturningStream(repositoryInformation.getRepositoryInterface())) {
                factory.addAdvice(SurroundingTransactionDetectorMethodInterceptor.INSTANCE);
            }
        });
        this.addRepositoryProxyPostProcessor(
                new MybatisRepositoryPrepareProcessor(mappingContext, sqlSessionTemplate.getConfiguration()));
        this.addRepositoryProxyPostProcessor(new RepositoryPrepareProcessor(mappingContext, sqlSessionTemplate.getConfiguration()));
        this.addQueryCreationListener(
                new MybatisQueryPrepareProcessor(mappingContext, sqlSessionTemplate.getConfiguration()));

    }

    private static boolean hasMethodReturningStream(Class<?> repositoryClass) {

        Method[] methods = ReflectionUtils.getAllDeclaredMethods(repositoryClass);

        for (Method method : methods) {
            if (Stream.class.isAssignableFrom(method.getReturnType())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public <T, ID> MybatisEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return (MybatisEntityInformation<T, ID>) MybatisEntityInformationSupport.getEntityInformation(domainClass,
                this.mappingContext);
    }

    @Override
    protected final MybatisRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation metadata) {
        MybatisRepositoryImplementation<?, ?> repository = this.getTargetRepository(metadata, this.sqlSessionTemplate);
        repository.setEscapeCharacter(this.escapeCharacter);
        return repository;
    }

    protected MybatisRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
                                                                        SqlSessionTemplate sqlSessionTemplate) {
        MybatisEntityInformation<?, Serializable> entityInformation = this
                .getEntityInformation(information.getDomainType());
        Object repository = this.getTargetRepositoryViaReflection(information, entityInformation, information,
                sqlSessionTemplate);
        Assert.isInstanceOf(MybatisRepositoryImplementation.class, repository);
        return (MybatisRepositoryImplementation<?, ?>) repository;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return DefaultMybatisRepository.class;
    }

    @Override
    protected ProjectionFactory getProjectionFactory(ClassLoader classLoader, BeanFactory beanFactory) {
        return super.getProjectionFactory(classLoader, beanFactory);
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable QueryLookupStrategy.Key key,
                                                                   QueryMethodEvaluationContextProvider evaluationContextProvider) {
        return Optional.of(MybatisQueryLookupStrategy.create(this.sqlSessionTemplate, this.mappingContext, key,
                evaluationContextProvider, this.escapeCharacter));
    }

    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
        return super.getRepositoryFragments(metadata);
    }

    public void setEntityPathResolver(EntityPathResolver entityPathResolver) {
        Assert.notNull(entityPathResolver, "EntityPathResolver must not be null.");
        this.entityPathResolver = entityPathResolver;
    }

    public void setEscapeCharacter(EscapeCharacter escapeCharacter) {
        this.escapeCharacter = escapeCharacter;
    }
}
