package io.github.springroe.data.mybatis.repository.query.expression;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class AliasedProjectionExpression extends ProjectionExpression {

    private ProjectionExpression projectionExpression;

    private String alias;

    public AliasedProjectionExpression(ProjectionExpression projectionExpression, String alias) {
        this.projectionExpression = projectionExpression;
        this.alias = alias;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        String propertyName = projectionExpression.getPropertyName();
        if (propertyName.contains("*")) {
            return projectionExpression.interpret(context);
        }
        return projectionExpression.interpret(context) + " AS " + alias;
    }

    public String getAlias() {
        return alias;
    }
}
