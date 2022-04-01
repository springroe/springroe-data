package io.github.springroe.data.mybatis.domain.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class CompoundCondition extends AbstractConditionImpl implements Condition {

    private BooleanOperator operator;

    private List<Condition> conditions;

    public CompoundCondition(BooleanOperator operator) {
        super(ConditionType.Compound);
        this.conditions = new ArrayList<>();
        this.operator = operator;
    }

    public CompoundCondition(List<Condition> conditions, BooleanOperator operator) {
        this(operator);
        this.applyCondition(conditions);
    }

    public CompoundCondition(BooleanOperator operator, Condition... conditions) {
        this(operator);
        this.applyCondition(conditions);
    }

    private void applyCondition(Condition... conditions) {
        this.conditions.clear();
        this.conditions = Arrays.asList(conditions);
    }

    private void applyCondition(List<Condition> conditions) {
        this.conditions.clear();
        this.conditions.addAll(conditions);
    }

    public static BooleanOperator reverseOperator(BooleanOperator operator) {
        return operator == BooleanOperator.AND ? BooleanOperator.OR : BooleanOperator.AND;
    }

    @Override
    public BooleanOperator getOperator() {
        return this.operator;
    }

    @Override
    public List<Condition> getConditions() {
        return this.conditions;
    }

    @Override
    public Condition not() {
        return new NegatedConditionWrapper(this);
    }

    @Override
    public boolean isJunction() {
        return true;
    }

    @Override
    public String render(boolean isNegated) {
        return null;
    }
}
