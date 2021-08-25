package com.emerson.bpm.event;

public interface StateChangeListener {

	enum RTM_STATE {	
		HOLD,
		READY		
	};
	
	void onStateChange(StateChangeEvent evt);
}
