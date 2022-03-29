package com.epam.esm.repository.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RepositoryException extends RuntimeException {
    private final String errorCode;
    private final String errorMsg;

}
