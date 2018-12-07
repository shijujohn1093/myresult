package com.cuckoo.myresults.model;

import java.io.Serializable;

public class Response<T> implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private T response;

	public Response(T response) {
		super();
		this.response = response;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}
	
	
	
	
}
