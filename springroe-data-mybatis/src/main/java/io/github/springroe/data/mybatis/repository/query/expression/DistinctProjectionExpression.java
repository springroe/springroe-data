package io.github.springroe.data.mybatis.repository.query.expression;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class DistinctProjectionExpression extends ProjectionExpression {

    private ProjectionExpression projectionExpression;

    public DistinctProjectionExpression(ProjectionExpression projectionExpression) {
        this.projectionExpression = projectionExpression;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        return "DISTINCT " + projectionExpression.interpret(context);
    }
}
