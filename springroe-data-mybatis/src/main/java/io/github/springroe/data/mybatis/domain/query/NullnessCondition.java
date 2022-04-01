package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class NullnessCondition extends AbstractSimpleCondition implements Condition {

    public NullnessCondition(String propertyName) {
        super(propertyName, ConditionType.Nullness);
    }

    public Condition not() {
        return new NegatedConditionWrapper(this);
    }

    @Override
    public String render(boolean isNegated) {
        return null;
    }

}
