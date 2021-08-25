package com.emerson.bpm.nodes.match.el;

public class ELException extends Exception {

	public ELException(String msg) {
			super(msg);
	}

	public ELException(Exception ex) {
		super(ex);
	}

	public ELException(String msg, Throwable cause) {
			super(msg, cause);
	}

}
