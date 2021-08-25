package com.emerson.bpm.engine;


import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.WorkingMemory;
import com.emerson.bpm.dsl.FieldProviderFactory;
import com.emerson.bpm.nodes.match.FieldNameComparator;
import com.emerson.bpm.util.ServiceFactory;
import com.picasso.paddle.Injector;
import com.picasso.paddle.PaddleConfig;
import com.picasso.paddle.annotation.Inject;
import com.picasso.paddle.annotation.Paddles;
import com.picasso.paddle.annotation.ProxyFactory;
import com.picasso.paddle.annotation.Service;
import com.picasso.paddle.api.PaddleContext;
import com.picasso.paddle.inject.PaddleAnnotationContext;
import com.picasso.paddle.util.PaddleConstants;

@Service
@Paddles(packagesToScan = "com.emerson.bpm.*")
public class EmersonSession extends DefaultSession {

	public SessionState StatefulSession = SessionState.StatefulSession;

	SessionState StatelessSession = SessionState.StatelessSession;

	private WorkingMemory wm;

	private Injector paddleInjector;

	@ProxyFactory
	FieldProviderFactory factory;

	@Inject(splprops="usejaxb=true")
	PaddleConfig config;
	
	private EmersonSession() {
		init();
	}

	private void init() {

		this.wm = new WorkingMemoryImpl();

		try {

			System.setProperty("PADDLES_CLASS", EmersonSession.class.getName());

			System.setProperty(PaddleConstants.SCAN_FOR_BEAN_PACKAGES, Boolean.toString(true));
			System.setProperty(PaddleConstants.BEAN_PACKAGE_SCAN_CLASS, FieldNameComparator.class.getName());

			System.setProperty(PaddleConstants.SCAN_FOR_PROXY_SERVICES, Boolean.toString(true));
			System.setProperty(PaddleConstants.PROXY_SERVICE_NAMES, FieldNameComparator.class.getName());

			PaddleContext context = new PaddleAnnotationContext();

			ServiceFactory.setSession(this);

			ServiceFactory.setFieldProviderFactory(factory);

			ServiceFactory.setInjector(paddleInjector);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Inject()
	public static Session begin(SessionState state) {

		if (state == SessionState.StatefulSession)
			return new EmersonStatefulSession();

		return new EmersonStatelessSession();

	}

	@Override
	public WorkingMemory getWorkingMemory() {
		return this.wm;
	}

	public static class EmersonStatefulSession extends EmersonSession {

		public EmersonStatefulSession() {
			super();
		}
	}

	public static class EmersonStatelessSession extends EmersonSession {

		public EmersonStatelessSession() {
			super();
		}

	}

}
