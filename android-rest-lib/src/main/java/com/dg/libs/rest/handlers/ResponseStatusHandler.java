package com.dg.libs.rest.handlers;

import com.dg.libs.rest.domain.ResponseStatus;

public interface ResponseStatusHandler {

	public boolean hasErrorInStatus(ResponseStatus status);

}