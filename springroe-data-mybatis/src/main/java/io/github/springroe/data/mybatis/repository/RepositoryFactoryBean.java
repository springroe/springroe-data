package io.github.springroe.data.mybatis.repository;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.data.mybatis.repository.query.EscapeCharacter;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class RepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
        extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {

    private @Nullable
    SqlSessionTemplate sqlSessionTemplate;

    private @Nullable
    MybatisMappingContext mappingContext;

    private EntityPathResolver entityPathResolver;

    private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;

    /**
     * Creates a new {@link TransactionalRepositoryFactoryBeanSupport} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    protected RepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.state(null != this.mappingContext, "MybatisMappingContext must not be null!");
        Assert.state(null != this.sqlSessionTemplate, "SqlSessionTemplate must not be null!");

        super.afterPropertiesSet();
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        return this.createRepositoryFactory(this.mappingContext, this.sqlSessionTemplate);
    }

    private RepositoryFactorySupport createRepositoryFactory(MybatisMappingContext mappingContext,
                                                             SqlSessionTemplate sqlSessionTemplate) {
        Assert.state(null != this.mappingContext, "MybatisMappingContext must not be null!");
        Assert.state(null != this.sqlSessionTemplate, "SqlSessionTemplate must not be null!");
        RepositoryFactory repositoryFactory = new RepositoryFactory(mappingContext, sqlSessionTemplate);
        repositoryFactory.setEntityPathResolver(this.entityPathResolver);
        repositoryFactory.setEscapeCharacter(this.escapeCharacter);
        return repositoryFactory;
    }

    @Override
    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);
        this.mappingContext = (MybatisMappingContext) mappingContext;
    }

    public void setEscapeCharacter(char escapeCharacter) {
        this.escapeCharacter = EscapeCharacter.of(escapeCharacter);
    }

    public void setSqlSessionTemplate(@Nullable SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Autowired
    public void setEntityPathResolver(ObjectProvider<EntityPathResolver> resolver) {
        this.entityPathResolver = resolver.getIfAvailable(() -> SimpleEntityPathResolver.INSTANCE);
    }

}
