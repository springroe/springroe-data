package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class LikeCondition extends AbstractSimpleCondition {

    private final Object value;

    private final MatchMode matchMode;

    public LikeCondition(String propertyName, Object value, MatchMode matchMode) {
        super(propertyName, ConditionType.Like);
        this.value = value;
        this.matchMode = matchMode;
    }

    @Override
    public String render(boolean isNegated) {
        return null;
    }

    public Object getValue() {
        return value;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }
}
