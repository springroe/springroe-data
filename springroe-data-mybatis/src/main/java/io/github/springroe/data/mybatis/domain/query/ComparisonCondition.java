package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class ComparisonCondition extends AbstractSimpleCondition {

    private final ComparisonOperator comparisonOperator;

    private final Object value;

    public ComparisonCondition(String propertyName, Object value, ComparisonOperator comparisonOperator) {
        super(propertyName, AbstractConditionImpl.ConditionType.Comparison);
        this.value = value;
        this.comparisonOperator = comparisonOperator;
    }

    public static enum ComparisonOperator {
        EQUAL {
            public ComparisonOperator negated() {
                return NOT_EQUAL;
            }

            public String rendered() {
                return " = ";
            }
        },
        NOT_EQUAL {
            public ComparisonOperator negated() {
                return EQUAL;
            }

            public String rendered() {
                return " <> ";
            }
        },
        LESS_THAN {
            public ComparisonOperator negated() {
                return GREATER_THAN_OR_EQUAL;
            }

            public String rendered() {
                return " < ";
            }
        },
        LESS_THAN_OR_EQUAL {
            public ComparisonOperator negated() {
                return GREATER_THAN;
            }

            public String rendered() {
                return " <= ";
            }
        },
        GREATER_THAN {
            public ComparisonOperator negated() {
                return LESS_THAN_OR_EQUAL;
            }

            public String rendered() {
                return " > ";
            }
        },
        GREATER_THAN_OR_EQUAL {
            public ComparisonOperator negated() {
                return LESS_THAN;
            }

            public String rendered() {
                return " >= ";
            }
        };

        public abstract ComparisonOperator negated();

        public abstract String rendered();
    }

    @Override
    public String render(boolean isNegated) {
        return null;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public Object getValue() {
        return value;
    }
}
