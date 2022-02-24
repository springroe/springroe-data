package io.github.springroe.data.jpa.domain;

import io.github.springroe.data.core.domain.Parentable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public abstract class AbstractParentableEntity<P extends Parentable<P, String>> extends AbstractJpaStringEntity implements Parentable<P, String> {

    @Schema(description = "parent id")
    protected String parentId;

    @Schema(description = "parent")
    protected P parent;

    @Schema(description = "parentPath like this 'parentId1,parentId2....'")
    protected String parentPath;

    @Schema(description = "children")
    protected List<P> children;

}
