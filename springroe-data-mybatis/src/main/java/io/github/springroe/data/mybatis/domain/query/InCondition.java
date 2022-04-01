package io.github.springroe.data.mybatis.domain.query;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class InCondition extends AbstractSimpleCondition {

    private final List<Object> values;

    public InCondition(String propertyName, List<Object> values) {
        super(propertyName, AbstractConditionImpl.ConditionType.In);
        this.values = values;
    }

    @Override
    public String render(boolean isNegated) {
        return null;
    }

    public List<Object> getValues() {
        return values;
    }
}
