package io.github.springroe.data.mybatis.domain.query;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public interface Condition {

    BooleanOperator getOperator();

    boolean isNegated();

    List<Condition> getConditions();

    Condition not();

    boolean isJunction();

    AbstractConditionImpl.ConditionType getConditionType();

    String render(boolean isNegated);

    public static enum BooleanOperator {
        AND,
        OR;
    }
}
