package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class AliasEntry {

    private String associationPath;

    private String alias;

    private JoinType joinType;

    public AliasEntry(String associationPath, String alias, JoinType joinType) {
        this.associationPath = associationPath;
        this.alias = alias;
        this.joinType = joinType;
    }

    public String getAssociationPath() {
        return associationPath;
    }

    public void setAssociationPath(String associationPath) {
        this.associationPath = associationPath;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }
}
