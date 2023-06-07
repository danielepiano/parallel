package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class WorkspaceNotFoundException extends ResourceNotFoundException {
    public WorkspaceNotFoundException(final Integer id, final Integer headquartersId) {
        super(new Error(
                        _ExceptionConstants.WORKSPACE_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKSPACE_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKSPACE_NOT_FOUND.getDetail(), id, headquartersId)
                )
        );
    }

    public WorkspaceNotFoundException(final Integer id, final Integer headquartersId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKSPACE_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKSPACE_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKSPACE_NOT_FOUND.getDetail(), id, headquartersId)
                ), cause
        );
    }
}
