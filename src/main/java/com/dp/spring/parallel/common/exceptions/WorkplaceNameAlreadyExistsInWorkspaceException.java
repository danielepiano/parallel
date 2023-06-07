package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class WorkplaceNameAlreadyExistsInWorkspaceException extends ConflictingResourceException {
    public WorkplaceNameAlreadyExistsInWorkspaceException(final String name, final Integer workspaceId) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_NAME_ALREADY_EXISTS_IN_WORKSPACE.getCode(),
                        _ExceptionConstants.WORKPLACE_NAME_ALREADY_EXISTS_IN_WORKSPACE.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_NAME_ALREADY_EXISTS_IN_WORKSPACE.getDetail(), name, workspaceId)
                )
        );
    }

    public WorkplaceNameAlreadyExistsInWorkspaceException(final String name, final Integer workspaceId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_NAME_ALREADY_EXISTS_IN_WORKSPACE.getCode(),
                        _ExceptionConstants.WORKPLACE_NAME_ALREADY_EXISTS_IN_WORKSPACE.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_NAME_ALREADY_EXISTS_IN_WORKSPACE.getDetail(), name, workspaceId)
                ), cause
        );
    }
}
