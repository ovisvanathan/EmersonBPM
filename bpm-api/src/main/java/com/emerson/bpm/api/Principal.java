package com.emerson.bpm.api;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface Principal  {

	List getProducts();
	
	List<AutonomousActor> getListOfGuests();

	Registrant getRegistrant();

	ExecutorService getExecutor();

	ExecutorService getExecutorMulti();

	void setGameAI(GameAI reteEngine);

	void setRegistrar(Registrant registrant);

	GameAI getGameAI();

	double getSales();

	int getNumGuests();

}
