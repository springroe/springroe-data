package io.github.springroe.data.jpa.criterion;

import jakarta.persistence.criteria.Path;

import java.util.Arrays;
import java.util.List;

public interface PathHelper {

    default Path<?> getPath(Path<?> root, String name) {
        return getPath(root, Arrays.stream(name.split("\\.")).toList());
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
