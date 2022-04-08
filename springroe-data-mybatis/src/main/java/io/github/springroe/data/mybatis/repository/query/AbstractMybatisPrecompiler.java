package io.github.springroe.data.mybatis.repository.query;

import com.samskivert.mustache.Mustache;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mybatis.dialect.Dialect;
import org.springframework.data.mybatis.dialect.internal.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.springframework.data.mybatis.dialect.internal.StandardDialectResolver;
import org.springframework.data.mybatis.mapping.MybatisMappingContext;
import org.springframework.data.mybatis.mapping.MybatisPersistentEntity;
import org.springframework.data.mybatis.repository.query.DefaultCollector;
import org.springframework.data.mybatis.repository.query.EscapeCharacter;
import org.springframework.data.mybatis.repository.query.MybatisPrecompiler;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Slf4j
public abstract class AbstractMybatisPrecompiler implements MybatisPrecompiler {

    protected final MybatisMappingContext mappingContext;

    protected final Configuration configuration;

    protected final String namespace;

    protected final MybatisPersistentEntity<?> persistentEntity;

    protected final Dialect dialect;

    protected Class<?> repositoryInterface;

    protected EscapeCharacter escape;

    private final Mustache.Compiler mustache;

    protected LanguageDriver languageDriver;

    AbstractMybatisPrecompiler(MybatisMappingContext mappingContext, Configuration configuration,
                               RepositoryInformation repositoryInformation) {

        this(mappingContext, configuration, repositoryInformation.getDomainType());
        this.repositoryInterface = repositoryInformation.getRepositoryInterface();
    }

    AbstractMybatisPrecompiler(MybatisMappingContext mappingContext, Configuration configuration, Class<?> domainType) {
        this.mappingContext = mappingContext;
        this.configuration = configuration;
        this.namespace = domainType.getName();
        this.persistentEntity = mappingContext.getRequiredPersistentEntity(domainType);
        this.dialect = StandardDialectResolver.INSTANCE.resolveDialect(
                new DatabaseMetaDataDialectResolutionInfoAdapter(configuration.getEnvironment().getDataSource()));
        this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
        this.mustache = Mustache.compiler().withLoader(name -> {
            String path = "org/springframework/data/mybatis/repository/query/template/" + name + ".mustache";
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        }).escapeHTML(false).withCollector(new DefaultCollector());
    }

    protected String render(String name, Object context) {
        String path = "io/github/springroe/data/mybatis/repository/query/template/" + name + ".mustache";
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            try (InputStreamReader source = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                return this.mustache.compile(source).execute(context);
            } catch (IOException ex) {
                throw new MappingException("Could not render the statement: " + name, ex);
            }
        } catch (IOException ex) {
            throw new MappingException("Could not render the statement: " + name, ex);
        }
    }

    @Override
    public void precompile() {
        String xml = this.doPrecompile();
        if (StringUtils.hasText(xml)) {
            this.compileMapper(this.namespace, xml);
        }
        List<MappedStatement> mappedStatements = doPrecompileMappedStatements();
        if (null != mappedStatements) {
            mappedStatements.forEach(ms -> {
                if (!configuration.hasStatement(ms.getId())) {
                    configuration.addMappedStatement(ms);
                }
            });
        }
    }

    protected void compileMapper(String namespace, String content) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"https://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n"
                + "<mapper namespace=\"" + namespace + "\">\n" + content + "\n</mapper>";
        if (log.isDebugEnabled()) {
            log.debug(xml);
        }
        try (InputStream inputStream = new ByteArrayInputStream(xml.getBytes())) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(inputStream, this.configuration,
                    this.namespace.replace('.', '/') + UUID.randomUUID().toString() + this.getResourceSuffix(),
                    this.configuration.getSqlFragments());
            mapperBuilder.parse();
        } catch (IOException ex) {
            throw new MappingException(ex.getMessage(), ex);
        }
    }

    protected String buildMappedStatementId(String statementName) {
        return this.namespace.replace('.', '/') + statementName;
    }

    protected MappedStatement compileMappedStatement(String id, String script, SqlCommandType sqlCommandType, List<ResultMap> resultMaps) {
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, script, null);
        return new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType).resultMaps(resultMaps).build();
    }

    protected MappedStatement compileMappedStatement(String id, String script, SqlCommandType sqlCommandType, ResultMap resultMap) {
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, script, null);
        return new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType).resultMaps(Arrays.asList(resultMap)).build();
    }

    protected abstract String doPrecompile();

    protected abstract List<MappedStatement> doPrecompileMappedStatements();

    protected String getResourceSuffix() {
        return ".precompile";
    }

}
