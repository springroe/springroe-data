package io.github.springroe.data.mybatis.repository.query.expression;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class CompoundProjectionExpression extends ProjectionExpression {

    private List<ProjectionExpression> projectionExpressions;

    public CompoundProjectionExpression(List<ProjectionExpression> projectionExpressions) {
        this.projectionExpressions = projectionExpressions;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < projectionExpressions.size(); i++) {
            ProjectionExpression projectionExpression = projectionExpressions.get(i);
            String propertyName = projectionExpression.getPropertyName();
            String sql = projectionExpression.interpret(context);
            sb.append(sql);
            if (!(projectionExpression instanceof AliasedProjectionExpression)) {
                if (!propertyName.contains("*")) {
                    String alias = projectionExpression.getPropertyAlias(context.getQueryCondition(), propertyName);
                    sb.append(" AS " + alias);
                }
            }
            if (i < projectionExpressions.size() - 1) {
                sb.append(", ");
            }
        }
        String selectFragment = sb.toString();
        context.appendSelectFragment(selectFragment);
        return selectFragment;
    }
}
