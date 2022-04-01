package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public enum MatchMode {

    EXACT {
        @Override
        public String toMatchString(String pattern) {
            return pattern;
        }

        @Override
        public String toConcatString(String pattern) {
            return "concat(" + pattern + ")";
        }
    },

    START {
        @Override
        public String toMatchString(String pattern) {
            return "'" + pattern + "%'";
        }

        @Override
        public String toConcatString(String pattern) {
            return "concat(" + pattern + ", '%')";
        }
    },

    END {
        @Override
        public String toMatchString(String pattern) {
            return "'%" + pattern + "'";
        }

        @Override
        public String toConcatString(String pattern) {
            return "concat('%', " + pattern + ")";
        }
    },

    ANYWHERE {
        @Override
        public String toMatchString(String pattern) {
            return "'%" + pattern + "%'";
        }

        @Override
        public String toConcatString(String pattern) {
            return "concat('%', " + pattern + ", '%')";
        }
    };

    public abstract String toMatchString(String pattern);

    public abstract String toConcatString(String pattern);
}
