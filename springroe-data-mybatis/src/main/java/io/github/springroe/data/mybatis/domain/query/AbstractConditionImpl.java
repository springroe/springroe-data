package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class AbstractConditionImpl implements Condition {

    protected ConditionType conditionType;

    protected AbstractConditionImpl(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    @Override
    public ConditionType getConditionType() {
        return conditionType;
    }

    @Override
    public boolean isNegated() {
        return false;
    }

    @Override
    public Condition not() {
        return new NegatedConditionWrapper(this);
    }

    public enum ConditionType {
        Between,
        Comparison,
        Compound,
        In,
        Like,
        Negated,
        Nullness
    }

}
