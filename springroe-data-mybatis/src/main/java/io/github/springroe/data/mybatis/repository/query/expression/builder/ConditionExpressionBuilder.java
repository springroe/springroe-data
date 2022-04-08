package io.github.springroe.data.mybatis.repository.query.expression.builder;

import io.github.springroe.data.mybatis.domain.query.*;
import io.github.springroe.data.mybatis.repository.query.expression.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class ConditionExpressionBuilder {

    private final static String NOT_NULL = "%s not allow null";

    private Condition condition;

    public ConditionExpressionBuilder(Condition condition) {
        Assert.notNull(condition, String.format(NOT_NULL, "condition"));
        this.condition = condition;
    }

    public QueryConditionExpression build() {
        return translator(condition);
    }

    private ConditionExpression translator(Condition condition) {
        ConditionExpression conditionExpression = null;
        Condition.BooleanOperator operator = null;
        List<ConditionExpression> conditionExpressions = new ArrayList<>();
        AbstractConditionImpl.ConditionType conditionType = condition.getConditionType();
        switch (conditionType) {
            case Comparison:
                conditionExpression = new ComparisonConditionExpression((ComparisonCondition) condition);
                break;
            case In:
                conditionExpression = new InConditionExpression((InCondition) condition);
                break;
            case Like:
                conditionExpression = new LikeConditionExpression((LikeCondition) condition);
                break;
            case Nullness:
                conditionExpression = new NullnessConditionExpression((NullnessCondition) condition);
                break;
            case Between:
                conditionExpression = new BetweenConditionExpression((BetweenCondition) condition);
                break;
            case Compound:
                CompoundCondition compoundCondition = (CompoundCondition) condition;
                operator = compoundCondition.getOperator();
                List<Condition> conditions = compoundCondition.getConditions();
                for (Condition c : conditions) {
                    conditionExpressions.add(translator(c));
                }
                conditionExpression = new CompoundConditionExpression(conditionExpressions, operator);
                break;
            case Negated:
                NegatedConditionWrapper negatedConditionWrapper = (NegatedConditionWrapper) condition;
                condition = negatedConditionWrapper.getCondition();
                conditions = negatedConditionWrapper.getConditions();
                operator = negatedConditionWrapper.getOperator();
                if (conditions.size() > 0) {
                    for (Condition c : conditions) {
                        conditionExpressions.add(translator(c));
                    }
                    conditionExpression = new CompoundConditionExpression(conditionExpressions, operator);
                } else {
                    conditionExpression = translator(condition).not();
                }
                break;
        }
        return conditionExpression;
    }

}
