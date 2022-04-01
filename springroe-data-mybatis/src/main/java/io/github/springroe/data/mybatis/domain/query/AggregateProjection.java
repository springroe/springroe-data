package io.github.springroe.data.mybatis.domain.query;

import org.springframework.util.Assert;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class AggregateProjection extends SimpleProjection {

    private final Function function;

    AggregateProjection(Function function, String propertyName) {
        super(propertyName, AbstractProjection.ProjectionType.AggregateProjection);
        Assert.notNull(function, "function not allow null");
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public enum Function {
        Count {
            @Override
            public String toString() {
                return "COUNT";
            }
        },
        Avg {
            @Override
            public String toString() {
                return "AVG";
            }
        },
        Max {
            @Override
            public String toString() {
                return "MAX";
            }
        },
        Min {
            @Override
            public String toString() {
                return "MIN";
            }
        };

        public abstract String toString();
    }

}
