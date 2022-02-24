package io.github.springroe.data.core.domain;

import java.io.Serializable;
import java.util.List;

/**
 * tree data entity support
 *
 * @param <P>  type of parent
 * @param <ID> type of primary key
 * @author kongsiyu
 */
public interface Parentable<P extends Parentable<P, ID>, ID extends Serializable> extends Persistable<ID> {

    /**
     * 获取上级ID
     *
     * @return parentId
     */
    public ID getParentId();

    /**
     * @param parentId parentId
     */
    public void setParentId(ID parentId);

    /**
     * @return parent
     */
    public P getParent();

    /**
     * @param parent parent
     */
    public void setParent(P parent);

    /**
     * @return parentPath
     */
    public String getParentPath();


    /**
     * @param parentPath parentPath
     */
    public void setParentPath(String parentPath);

    /**
     * @return children
     */
    public List<P> getChildren();

    /**
     * @param children children
     */
    public void setChildren(List<P> children);
}
