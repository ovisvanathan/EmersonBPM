package com.emerson.bpm.api;

import com.emerson.bpm.api.AutonomousActor.SubjectType;

public class OTNCoverNode<T extends AutonomousActor> extends OTNSubject<T> {
	
	Principal pub;
	Principal p;
	ParticleType parType;
	
	GameAI reteEngine;
	
	public OTNCoverNode(T data) {
		super(data);
	}

	public OTNCoverNode(T data, SubjectType s) {
		super(data, s);
	}

	public OTNCoverNode(T data, SubjectType s, Principal p, ParticleType parType) {
		super(data, s);
		this.p = p;
		this.parType = parType;
	}

	
	public OTNCoverNode setPrincipal(Principal pub) {
		this.pub = pub;
		return this;
	}

	public void setGameAI(GameAI reteEngine) {
		this.reteEngine = reteEngine;
		p.setGameAI(reteEngine);
	}
	
	
	
}
