package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public enum JoinType {

    INNER_JOIN("INNER JOIN"), LEFT_JOIN("LEFT JOIN"), RIGHT_JOIN("RIGHT JOIN");

    private String joinType;

    JoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }
}
