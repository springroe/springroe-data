package io.github.springroe.data.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public abstract class AbstractException extends RuntimeException {
    @Getter
    @Setter
    private String errorCode = null;

}
