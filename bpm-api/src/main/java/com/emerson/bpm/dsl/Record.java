package com.emerson.bpm.dsl;

public interface Record {

	default public Class getRequestType() { return Record.class; }
}
