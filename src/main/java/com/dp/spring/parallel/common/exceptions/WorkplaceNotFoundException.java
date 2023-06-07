package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class WorkplaceNotFoundException extends ResourceNotFoundException {
    public WorkplaceNotFoundException(final Integer id, final Integer workspaceId) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKPLACE_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_NOT_FOUND.getDetail(), id, workspaceId)
                )
        );
    }

    public WorkplaceNotFoundException(final Integer id, final Integer workspaceId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKPLACE_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_NOT_FOUND.getDetail(), id, workspaceId)
                ), cause
        );
    }
}
