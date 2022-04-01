package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class BetweenCondition extends AbstractSimpleCondition {

    private final Object lowerBound;

    private final Object upperBound;

    private BoundType boundType;

    public BetweenCondition(String propertyName, Object lowerBound, Object upperBound, BoundType boundType) {
        super(propertyName, AbstractConditionImpl.ConditionType.Between);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.boundType = boundType;
    }

    @Override
    public String render(boolean isNegated) {
        return null;
    }

    public Object getLowerBound() {
        return lowerBound;
    }

    public Object getUpperBound() {
        return upperBound;
    }

    public enum BoundType {
        Date,
        Integer,
        Long,
        Short,
        Double,
        Float
    }

    public BoundType getBoundType() {
        return boundType;
    }

}
