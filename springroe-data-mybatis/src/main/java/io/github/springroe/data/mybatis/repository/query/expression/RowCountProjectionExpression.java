package io.github.springroe.data.mybatis.repository.query.expression;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class RowCountProjectionExpression extends ProjectionExpression {

    @Override
    public String interpret(MybatisQueryContext context) {
        return "COUNT(1)";
    }
}
