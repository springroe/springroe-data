package io.github.springroe.data.mybatis.domain.query;

import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public abstract class AbstractSimpleCondition extends AbstractConditionImpl {

    private final String propertyName;

    private static final List<Condition> NO_CONDITIONS = Collections.emptyList();

    public AbstractSimpleCondition(String propertyName, ConditionType conditionType) {
        super(conditionType);
        this.propertyName = propertyName;
    }

    @Override
    public List<Condition> getConditions() {
        return NO_CONDITIONS;
    }

    @Override
    public BooleanOperator getOperator() {
        return BooleanOperator.AND;
    }

    public boolean isJunction() {
        return false;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
