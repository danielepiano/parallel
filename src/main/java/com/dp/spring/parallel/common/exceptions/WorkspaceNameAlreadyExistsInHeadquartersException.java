package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class WorkspaceNameAlreadyExistsInHeadquartersException extends ConflictingResourceException {
    public WorkspaceNameAlreadyExistsInHeadquartersException(final String name, final Integer headquartersId) {
        super(new Error(
                        _ExceptionConstants.WORKSPACE_NAME_ALREADY_EXISTS_IN_HEADQUARTERS.getCode(),
                        _ExceptionConstants.WORKSPACE_NAME_ALREADY_EXISTS_IN_HEADQUARTERS.getTitle(),
                        String.format(_ExceptionConstants.WORKSPACE_NAME_ALREADY_EXISTS_IN_HEADQUARTERS.getDetail(), name, headquartersId)
                )
        );
    }

    public WorkspaceNameAlreadyExistsInHeadquartersException(final String name, final Integer headquartersId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKSPACE_NAME_ALREADY_EXISTS_IN_HEADQUARTERS.getCode(),
                        _ExceptionConstants.WORKSPACE_NAME_ALREADY_EXISTS_IN_HEADQUARTERS.getTitle(),
                        String.format(_ExceptionConstants.WORKSPACE_NAME_ALREADY_EXISTS_IN_HEADQUARTERS.getDetail(), name, headquartersId)
                ), cause
        );
    }
}
