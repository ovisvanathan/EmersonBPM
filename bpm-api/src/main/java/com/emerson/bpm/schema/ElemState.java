package com.emerson.bpm.schema;

import java.util.List;

public class ElemState {

	public enum ELEM_STATES {
		MG_SIZE_KNOWN, PARTICLE_NAME_KNOWN, ALL_PARTICLES_KNOWN, ALL_ATTRS_KNOWN, ALL_DETAILS_KNOWN
	};


	public String elemName;

	public ELEM_STATES est;

	public int mgsize;

	public int currval;

	public List<AttrInfo> attrs;
	
	public List<String> elems;
	
	public String[] particles;

	public boolean isComplex;

	public ElemState next;

}

