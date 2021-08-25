package com.emerson.bpm.api;

import java.util.HashMap;
import java.util.Map;

//abstract class to represent top level entity that
// will receive and send events
public class Registrant<T extends AutonomousActor> {
	
	Principal protagon;
	
	Map<String, T> entityMap = new HashMap();
	
	public Principal getPrincipal() {
		return protagon;
	}

	public Registrant(Principal protagon) {
		this.protagon = protagon;
		protagon.setRegistrar(this);
	}
	
	public T getEntity(String c_id) {
		return (T) ((AutonomousActor)protagon).getEntity(c_id);
	}

	public OTNSubject wrap(AutonomousActor actor) {
		OTNCoverNode<T> otnActor = new OTNCoverNode(actor, actor.getSubjectType());
		entityMap.put((String) actor.getKey(), (T) otnActor);
		return otnActor;
	}

}
