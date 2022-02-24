package io.github.springroe.data.jpa.criterion;

import javax.persistence.criteria.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface PathHelper {

    default Path<?> getPath(Path<?> root, String name) {
        return getPath(root, Arrays.stream(name.split("\\.")).collect(Collectors.toList()));
    }

    default Path<?> getPath(Path<?> root, List<String> names) {
        Path<?> path = root.get(names.get(0));
        if (names.size() == 1) {
            return path;
        } else {
            return getPath(path, names.subList(1, names.size()));
        }
    }
}
