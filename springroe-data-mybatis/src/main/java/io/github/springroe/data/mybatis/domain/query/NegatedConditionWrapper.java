package io.github.springroe.data.mybatis.domain.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class NegatedConditionWrapper extends AbstractConditionImpl implements Condition {

    private final Condition condition;

    private final BooleanOperator negatedOperator;

    private final List<Condition> negatedConditions;

    public NegatedConditionWrapper(Condition condition) {
        super(ConditionType.Negated);
        this.condition = condition;
        this.negatedOperator = condition.isJunction() ? CompoundCondition.reverseOperator(condition.getOperator()) : condition.getOperator();
        this.negatedConditions = negateCompoundConditions(condition.getConditions());
    }

    private static List<Condition> negateCompoundConditions(List<Condition> conditions) {
        if (conditions != null && !conditions.isEmpty()) {
            List<Condition> negatedExpressions = new ArrayList<Condition>();
            Iterator iterator = conditions.iterator();
            while (iterator.hasNext()) {
                Condition condition = (Condition) iterator.next();
                negatedExpressions.add(condition.not());
            }
            return negatedExpressions;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public BooleanOperator getOperator() {
        return this.negatedOperator;
    }

    @Override
    public boolean isNegated() {
        return !this.condition.isNegated();
    }

    @Override
    public List<Condition> getConditions() {
        return this.negatedConditions;
    }

    @Override
    public Condition not() {
        return new NegatedConditionWrapper(this);
    }

    @Override
    public boolean isJunction() {
        return this.condition.isJunction();
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public String render(boolean isNegated) {
        return null;
    }
}
