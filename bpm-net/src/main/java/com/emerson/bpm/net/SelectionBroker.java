package com.emerson.bpm.net;

import java.util.HashMap;
import java.util.Map;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.Registrant;
import com.emerson.bpm.event.SelectionHandler;

class SelectionBroker<T extends AutonomousActor> {

			Map<Integer, SelectionHandler> socketHandlersMap = new HashMap();

			String[] pubevents;
			
			Map<Integer, SelectionHandler> KeyHandlersMap = new HashMap();

			Registrant<T> registrar;

			public SelectionBroker(Registrant<T> reg, String[] pubevents) {
				this.registrar =  reg;
				this.pubevents = pubevents;
			}

			public SelectionHandler keyToIdHandler(int c_id) {
				
		//		SelectionHandler h = KeyHandlersMap.get(c_id);				

			//	MemoryStore.put(c_id, );

				return (SelectionHandler) this.registrar;
			}
			
		}
			
