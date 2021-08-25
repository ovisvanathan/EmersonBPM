package com.emerson.bpm.subscribe;

import com.emerson.bpm.api.SDKNode;

public interface ObserverCallback {

	default public void onNextCB(Object value, SDKNode sdkNode) {}

}
